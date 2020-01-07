package se.iths.auktionera.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.auktionera.persistence.entity.AccountEntity;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAuthId(String authId);

    Optional<AccountEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
