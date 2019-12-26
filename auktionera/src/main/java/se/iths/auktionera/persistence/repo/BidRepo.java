package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.persistence.entity.BidEntity;

import java.util.List;

@Repository
public interface BidRepo extends JpaRepository<BidEntity, Long> {
    List<BidEntity> findAllByAuctionIdOrderByBidAt(long auctionId);

    int countAllByBidderId(long bidderId);
}
