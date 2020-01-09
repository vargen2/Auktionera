package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.persistence.entity.ImageEntity;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {
}
