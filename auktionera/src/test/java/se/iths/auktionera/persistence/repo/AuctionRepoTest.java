package se.iths.auktionera.persistence.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.entity.AuctionEntity;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class AuctionRepoTest {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AuctionRepo auctionRepo;


    @BeforeEach
    void setUp() {
        AccountEntity accountEntity = AccountEntity.builder()
                .anonymousBuyer(false)
                .authId("User")
                .city("City")
                .email("name@example.com")
                .postNr(12345)
                .streetName("Street 1")
                .userName("Usern")
                .createdAt(Instant.now())
                .build();
        accountRepo.saveAndFlush(accountEntity);
        AuctionEntity auctionEntity = AuctionEntity.builder().title("Stol").description("En bra stol").seller(accountEntity).build();
        auctionRepo.saveAndFlush(auctionEntity);
    }

    @Test
    void getAuctions() {
        List<AuctionEntity> all = auctionRepo.findAll();
        assertEquals(1, all.size());
    }

}