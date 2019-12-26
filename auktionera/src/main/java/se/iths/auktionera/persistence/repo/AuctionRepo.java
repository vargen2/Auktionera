package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;

import java.time.Instant;
import java.util.List;

@Repository
public interface AuctionRepo extends JpaRepository<AuctionEntity, Long>, JpaSpecificationExecutor<AuctionEntity> {

    default List<AuctionEntity> findAllWhereStateInProgress() {
        return findAllByStateAndEndsAtBefore(AuctionState.InProgress, Instant.now());
    }

    List<AuctionEntity> findAllByStateAndEndsAtBefore(AuctionState state, Instant instant);

    int countAllBySeller_Id(long sellerId);

    int countAllByBuyer_Id(long buyerId);

    default int countAllSoldBySeller(long sellerId) {
        return countAllBySeller_IdAndStateOrSeller_IdAndState(sellerId, AuctionState.EndedWithBuyout, sellerId, AuctionState.EndedBought);
    }

    int countAllBySeller_IdAndStateOrSeller_IdAndState(long sellerId, AuctionState state, long sellerId2, AuctionState state2);
}
