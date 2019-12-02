package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.auktionera.persistence.entity.AuctionEntity;

public interface AuctionRepo extends JpaRepository<AuctionEntity, Long> {

}
