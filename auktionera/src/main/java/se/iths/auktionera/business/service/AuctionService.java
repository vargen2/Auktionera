package se.iths.auktionera.business.service;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuctionService implements IAuctionService {

    private final AccountRepo accountRepo;
    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;

    public AuctionService(AccountRepo accountRepo, AuctionRepo auctionRepo, BidRepo bidRepo) {
        this.accountRepo = accountRepo;
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
    }

    @Override
    public List<Auction> getAuctions(Map<String, String> filters, Map<String, String> sorters) {
        return auctionRepo.findAll().stream().map(Auction::new).collect(Collectors.toList());
    }

    @Override
    public Auction getAuction(long id) {
        return new Auction(auctionRepo.findById(id).orElseThrow());
    }

    @Override
    public Auction createAuction(String authId, CreateAuctionRequest auctionRequest) {
        var seller = accountRepo.findByAuthId(authId).orElseThrow();
        var auctionEntity = new AuctionEntity(auctionRequest, seller);

        return new Auction(auctionRepo.saveAndFlush(auctionEntity));
    }

    @Override
    public Auction createBid(String authId, CreateBidRequest bidRequest) {
        Objects.requireNonNull(bidRequest);
        Validate.isTrue(bidRequest.getBidPrice() > bidRequest.getCurrentPrice(), "Bid has to be greater than current price.");

        var bidder = accountRepo.findByAuthId(authId).orElseThrow();
        var auctionEntity = auctionRepo.findById(bidRequest.getAuctionId()).orElseThrow();
        Validate.isTrue(auctionEntity.getState() == AuctionState.InProgress);
        Validate.isTrue(bidder.getId() != auctionEntity.getSeller().getId(), "Bidder can't be same as Seller.");

        //Todo måste kolla endsat och endeda
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

        if (auctionEntity.getBuyoutPrice() > 0 && bidEntity.getBidPrice() >= auctionEntity.getBuyoutPrice()) {
            auctionEntity.setState(AuctionState.EndedBought);
            auctionEntity.setEndedAt(Instant.now());
            auctionRepo.saveAndFlush(auctionEntity);
        }

        return new Auction(auctionEntity, bidEntity);
    }


}
