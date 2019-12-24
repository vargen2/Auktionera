package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.persistence.entity.ReviewEntity;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByAuction_Id(long auctionId);

    boolean existsByAuction_IdAndSeller_IdAndCreatedBySellerTrue(long auctionId, long sellerId);

    boolean existsByAuction_IdAndBuyer_IdAndCreatedBySellerFalse(long auctionId, long buyerId);
}
