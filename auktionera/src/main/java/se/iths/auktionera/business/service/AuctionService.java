package se.iths.auktionera.business.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.business.model.EmailNotification;
import se.iths.auktionera.business.query.AuctionSort;
import se.iths.auktionera.business.query.AuctionSpecification;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;
import se.iths.auktionera.persistence.entity.ImageEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ImageRepo;
import se.iths.auktionera.worker.INotificationSender;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuctionService implements IAuctionService {

    private static final Logger log = LoggerFactory.getLogger(AuctionService.class);

    private final AccountRepo accountRepo;
    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;
    private final ImageRepo imageRepo;
    private final INotificationSender notificationSender;

    public AuctionService(AccountRepo accountRepo, AuctionRepo auctionRepo, BidRepo bidRepo, ImageRepo imageRepo, INotificationSender notificationSender) {
        this.accountRepo = accountRepo;
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
        this.imageRepo = imageRepo;
        this.notificationSender = notificationSender;
    }

    @Override
    public List<Auction> getAuctions(Map<String, String> filters, Map<String, String> sorters) {

        Sort sort = sorters == null ? Sort.unsorted() : AuctionSort.create(sorters);

        if (filters == null) {
            return auctionRepo.findAll(sort).stream().map(Auction::new).collect(Collectors.toList());
        }

        Specification<AuctionEntity> specification = AuctionSpecification.create(filters);

        return auctionRepo.findAll(specification, sort).stream().map(Auction::new).collect(Collectors.toList());
    }

    @Override
    public Auction getAuction(long id) {
        return new Auction(auctionRepo.findById(id).orElseThrow());
    }

    @Override
    public Auction createAuction(String authId, CreateAuctionRequest auctionRequest) {
        var seller = accountRepo.findByAuthId(authId).orElseThrow();

        List<ImageEntity> imageEntities = auctionRequest.getImageIds() != null ? imageRepo.findAllById(auctionRequest.getImageIds()) : List.of();

        for (var entity : imageEntities) {
            Validate.isTrue(entity.getCreator().getId() == seller.getId(), "Can only use own images.");
            Validate.isTrue(entity.getAuction() == null, "Image already in use.");
        }

        var auctionEntity = new AuctionEntity(auctionRequest, seller);
        auctionRepo.saveAndFlush(auctionEntity);

        for (var entity : imageEntities) {
            entity.setAuction(auctionEntity);
        }
        imageRepo.saveAll(imageEntities);
        imageRepo.flush();

        var auction = new Auction(auctionEntity);
        log.info("Auction created: {}", auction);
        return auction;
    }

    @Override
    public Auction createBid(String authId, CreateBidRequest bidRequest) {
        Objects.requireNonNull(bidRequest);
        Validate.isTrue(bidRequest.getBidPrice() > bidRequest.getCurrentPrice(), "Bid has to be greater than current price.");

        var bidder = accountRepo.findByAuthId(authId).orElseThrow();
        var auctionEntity = auctionRepo.findById(bidRequest.getAuctionId()).orElseThrow();
        Validate.isTrue(auctionEntity.getState() == AuctionState.InProgress, "Auction ended.");
        Validate.isTrue(bidder.getId() != auctionEntity.getSeller().getId(), "Bidder can't be same as Seller.");
        Validate.isTrue(Instant.now().isBefore(auctionEntity.getEndsAt()), "Auction expired.");
        Validate.isTrue(auctionEntity.getEndedAt() == null, "Something went wrong.");


        var previousBids = bidRepo.findAllByAuctionIdOrderByBidAt(auctionEntity.getId());

        long previousBidId = 0;
        if (previousBids.size() > 0) {
            BidEntity lastBid = previousBids.get(previousBids.size() - 1);
            Validate.isTrue(bidder.getId() != lastBid.getBidder().getId(), "Bidder can't bid on own bid.");
            Validate.isTrue(bidRequest.getCurrentPrice() == lastBid.getBidPrice(), "Bid request current price don't match latest bid.");
            Validate.isTrue(bidRequest.getBidPrice() >= lastBid.getBidPrice() * 1.05, "Bid to low.");
            previousBidId = lastBid.getId();
        } else {
            Validate.isTrue(bidRequest.getCurrentPrice() == auctionEntity.getStartPrice(), "Bid request current price don't match start price.");
            Validate.isTrue(bidRequest.getBidPrice() >= auctionEntity.getStartPrice() * 1.05, "Bid to low.");
        }

        BidEntity bidEntity = new BidEntity(previousBidId, bidRequest, auctionEntity, bidder);
        bidRepo.saveAndFlush(bidEntity);


        if (previousBids.size() > 0) {
            var lastBid = previousBids.get(previousBids.size() - 1);
            var previousBidder = lastBid.getBidder();
            if (previousBidder.isReceiveEmailWhenOutbid()) {
                notificationSender.enqueueEmailNotification(new EmailNotification(previousBidder.getEmail(),
                        "Someone placed higher bid on auction " + auctionEntity.getId() + ". New bid is " + bidEntity.getBidPrice()));
            }
        }

        if (auctionEntity.getBuyoutPrice() > 0 && bidEntity.getBidPrice() >= auctionEntity.getBuyoutPrice()) {
            auctionEntity.setState(AuctionState.EndedWithBuyout);
            auctionEntity.setEndedAt(Instant.now());
            auctionEntity.setBuyer(bidEntity.getBidder());
            auctionRepo.saveAndFlush(auctionEntity);
            if (bidEntity.getBidder().isReceiveEmailWhenWon()) {
                notificationSender.enqueueEmailNotification(
                        new EmailNotification(bidEntity.getBidder().getEmail(),
                                "You won auction " + auctionEntity.getId()));
            }
            if (auctionEntity.getSeller().isReceiveEmailWhenSold()) {
                notificationSender.enqueueEmailNotification(
                        new EmailNotification(auctionEntity.getSeller().getEmail(), "Your auction sold " + auctionEntity.getId()));
            }
        }

        Instant tenMinutesIntoFuture = Instant.now().plus(10, ChronoUnit.MINUTES);
        if (auctionEntity.getState() == AuctionState.InProgress && auctionEntity.getEndsAt().isBefore(tenMinutesIntoFuture)) {
            auctionEntity.setEndsAt(tenMinutesIntoFuture);
            auctionRepo.saveAndFlush(auctionEntity);
        }

        var auction = new Auction(auctionEntity, bidEntity);
        log.info("Bid created: {}", auction);
        return auction;
    }


}
