package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.auktionera.persistence.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAuthId(String authId);
}
