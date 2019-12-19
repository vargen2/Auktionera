package se.iths.auktionera.business.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;

import java.time.Instant;
import java.util.List;

@Component
@Scope("prototype")
public class EndAuctionsTask implements IEndAuctionsTask {

    private static final Logger log = LoggerFactory.getLogger(EndAuctionsTask.class);

    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;

    public EndAuctionsTask(AuctionRepo auctionRepo, BidRepo bidRepo) {
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
    }

    @Override
    public void endAuctions() {
        List<AuctionEntity> auctions = auctionRepo.findAllWhereStateInProgress();
        log.info("Found {} auctions to end.", auctions.size());

        for (var auction : auctions) {
            auction.setEndedAt(Instant.now());
            List<BidEntity> bids = bidRepo.findAllByAuctionIdOrderByBidAt(auction.getId());

            if (bids != null && bids.size() > 0) {
                auction.setState(AuctionState.EndedBought);
            } else {
                auction.setState(AuctionState.EndedNotBought);
            }
            auctionRepo.saveAndFlush(auction);
            log.info("Auction {} {}.", auction.getId(), auction.getState());
        }
    }
}
