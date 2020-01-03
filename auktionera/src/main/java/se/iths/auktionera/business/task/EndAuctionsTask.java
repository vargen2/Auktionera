package se.iths.auktionera.business.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.model.EmailNotification;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.worker.INotificationSender;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class EndAuctionsTask implements IEndAuctionsTask {

    private static final Logger log = LoggerFactory.getLogger(EndAuctionsTask.class);

    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;
    private final INotificationSender notificationSender;

    private int counter;

    public EndAuctionsTask(AuctionRepo auctionRepo, BidRepo bidRepo, INotificationSender notificationSender) {
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
        this.notificationSender = notificationSender;
    }

    @Override
    public void endAuctions() {
        List<Long> auctionIds = auctionRepo.findAllWhereStateInProgress()
                .stream()
                .map(AuctionEntity::getId)
                .collect(Collectors.toList());

        log.info("Found {} auctions to end.", auctionIds.size());


        if (auctionIds.isEmpty()) {
            return;
        }

        for (var auction : auctionIds) {
            endAuction(auction);
        }

        log.info("Ended {} of {} auctions.", counter, auctionIds.size());
    }

    public void endAuction(long id) {
        try {
            AuctionEntity auction = auctionRepo.findById(id).orElseThrow();
            if (auction.getState() != AuctionState.InProgress) {
                log.warn("Auction {} is not in state {} when trying to end it.", id, AuctionState.InProgress);
                return;
            }

            List<BidEntity> bids = bidRepo.findAllByAuctionIdOrderByBidAt(auction.getId());

            if (bids != null && bids.size() > 0) {
                BidEntity lastBid = bids.get(bids.size() - 1);
                auction.setState(AuctionState.EndedBought);
                auction.setBuyer(lastBid.getBidder());
                if (lastBid.getBidder().isReceiveEmailWhenWon()) {
                    notificationSender.enqueueEmailNotification(
                            new EmailNotification(lastBid.getBidder().getEmail(), "You won auction " + auction.getId()));
                }
                if (auction.getSeller().isReceiveEmailWhenSold()) {
                    notificationSender.enqueueEmailNotification(
                            new EmailNotification(auction.getSeller().getEmail(), "Your auction sold " + auction.getId()));
                }
            } else {
                auction.setState(AuctionState.EndedNotBought);
            }
            auction.setEndedAt(Instant.now());
            auctionRepo.saveAndFlush(auction);
            counter++;
            log.info("Auction {} ended with state {}.", auction.getId(), auction.getState());
        } catch (Exception e) {
            log.error("Something went wrong when trying to end auction {}.", id, e);
        }
    }
}
