package se.iths.auktionera.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;

import java.time.Instant;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Account getAccount(String authId) {
        var account = accountRepo.findByAuthId(authId);
        if (account.isPresent()) {
            return new Account(account.get());
        }

        var newAccount = new Account(accountRepo.saveAndFlush(AccountEntity.builder().authId(authId).createdAt(Instant.now()).build()));
        log.info("Account created: {}", newAccount);
        return newAccount;
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
        Optional.ofNullable(updateAccountRequest.getReceiveEmailWhenReviewed()).ifPresent(acc::setReceiveEmailWhenReviewed);
        Optional.ofNullable(updateAccountRequest.getReceiveEmailWhenOutbid()).ifPresent(acc::setReceiveEmailWhenOutbid);

        var updatedAccount = new Account(accountRepo.saveAndFlush(acc));
        log.info("Account updated: {}", updatedAccount);
        return updatedAccount;
    }
}
