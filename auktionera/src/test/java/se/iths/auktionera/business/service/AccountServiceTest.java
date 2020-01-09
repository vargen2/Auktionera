package se.iths.auktionera.business.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.iths.auktionera.business.model.Account;
import se.iths.auktionera.business.model.UpdateAccountRequest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.security.UserPrincipal;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
class AccountServiceTest {

    @MockBean
    AccountRepo accountRepo;

    private IAccountService accountService;
    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepo);
        accountEntity = AccountEntity.builder()
                .id(10)
                .anonymousBuyer(false)
                .city("City")
                .email("name@example.com")
                .postNr(12345)
                .streetName("Street 1")
                .userName("Usern")
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void getAccount() {
        when(accountRepo.findById(accountEntity.getId())).thenReturn(Optional.of(accountEntity));
        Account account = accountService.getAccount(UserPrincipal.create(accountEntity));
        assertNotNull(account);
        assertEquals(accountEntity.getUserName(), account.getUser().getUserName());
    }

    @Test
    void updateAccountUserName() {
        when(accountRepo.findById(accountEntity.getId())).thenReturn(Optional.of(accountEntity));
        when(accountRepo.saveAndFlush(any(AccountEntity.class))).thenReturn(accountEntity);

        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setUserName("NewName");
        Map<String, String> fields = Map.of("userName", "NewName");
        Account account = accountService.updateAccount(UserPrincipal.create(accountEntity), updateAccountRequest);
        assertNotNull(account);
        assertEquals(fields.get("userName"), account.getUser().getUserName());
    }


}