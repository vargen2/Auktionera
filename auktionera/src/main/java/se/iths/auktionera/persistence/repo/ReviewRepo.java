package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.persistence.entity.ReviewEntity;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Long> {

    default List<ReviewEntity> findAllOnSeller(long userId) {
        return findAllBySeller_IdAndCreatedBySellerFalse(userId);
    }

    default List<ReviewEntity> findAllOnBuyer(long userId) {
        return findAllByBuyer_IdAndCreatedBySellerTrue(userId);
    }

    List<ReviewEntity> findAllBySeller_IdAndCreatedBySellerFalse(long sellerId);

    List<ReviewEntity> findAllByBuyer_IdAndCreatedBySellerTrue(long buyerId);

    boolean existsByAuction_IdAndSeller_IdAndCreatedBySellerTrue(long auctionId, long sellerId);

    boolean existsByAuction_IdAndBuyer_IdAndCreatedBySellerFalse(long auctionId, long buyerId);

}
