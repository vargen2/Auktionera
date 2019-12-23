package se.iths.auktionera.business.service;

import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;

import java.time.Instant;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Account getAccount(String authId) {
        AccountEntity acc = accountRepo.findByAuthId(authId)
                .orElseGet(() -> accountRepo.saveAndFlush(AccountEntity.builder().authId(authId).createdAt(Instant.now()).build()));

        return new Account(acc);
    }


    @Override
    public Account updateAccount(String authId, UpdateAccountRequest updateAccountRequest) {
        AccountEntity acc = accountRepo.findByAuthId(authId).orElseThrow();

        Optional.ofNullable(updateAccountRequest.getUserName()).ifPresent(acc::setUserName);
        Optional.ofNullable(updateAccountRequest.getEmail()).ifPresent(acc::setEmail);
        Optional.ofNullable(updateAccountRequest.getStreetName()).ifPresent(acc::setStreetName);
        Optional.ofNullable(updateAccountRequest.getCity()).ifPresent(acc::setCity);
        Optional.ofNullable(updateAccountRequest.getPostNr()).ifPresent(acc::setPostNr);
        Optional.ofNullable(updateAccountRequest.getAnonymousBuyer()).ifPresent(acc::setAnonymousBuyer);

        AccountEntity updated = accountRepo.saveAndFlush(acc);
        return new Account(updated);
    }
}
