package se.iths.auktionera.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.iths.auktionera.business.model.CreateAuctionRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class AuctionEntityTest {

    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        accountEntity = mock(AccountEntity.class);
    }

    @Test
    public void Create() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .title("En stol")
                .description("En bra stol")
                .buyoutPrice(1000)
                .startPrice(100)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        var auction = new AuctionEntity(request, accountEntity);

        assertNotNull(auction);
    }

    @Test
    public void CreateNoBuyout() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .title("En stol")
                .description("En bra stol")
                .startPrice(100)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        var auction = new AuctionEntity(request, accountEntity);

        assertNotNull(auction);
    }

    @Test
    public void CreateNoBuyoutZero() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .title("En stol")
                .description("En bra stol")
                .buyoutPrice(0)
                .startPrice(100)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        var auction = new AuctionEntity(request, accountEntity);

        assertNotNull(auction);
    }

    @Test
    public void FailEndDate() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .title("En stol")
                .description("En bra stol")
                .buyoutPrice(1000)
                .startPrice(100)
                .endsAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();


        assertThrows(IllegalArgumentException.class, () -> new AuctionEntity(request, accountEntity));

    }

    @Test
    public void FailBuyout() {
        CreateAuctionRequest request = CreateAuctionRequest.builder()
                .title("En stol")
                .description("En bra stol")
                .buyoutPrice(50)
                .startPrice(100)
                .endsAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();


        assertThrows(IllegalArgumentException.class, () -> new AuctionEntity(request, accountEntity));

    }

}