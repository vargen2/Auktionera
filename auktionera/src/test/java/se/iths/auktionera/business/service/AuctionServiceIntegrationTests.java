package se.iths.auktionera.business.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.enums.AuthProvider;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.*;
import se.iths.auktionera.security.UserPrincipal;
import se.iths.auktionera.worker.INotificationSender;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class AuctionServiceIntegrationTests {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ImageRepo imageRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @MockBean
    private INotificationSender notificationSender;

    private IAuctionService auctionService;

    private UserPrincipal userPrincipal1;
    private UserPrincipal userPrincipal2;
    private UserPrincipal userPrincipal3;

    @BeforeEach
    void setUp() {
        AccountEntity accountEntity = AccountEntity.builder()
                .anonymousBuyer(false)
                .provider(AuthProvider.google)
                .providerId("1")
                .city("City")
                .email("name@example.com")
                .postNr(12345)
                .streetName("Street 1")
                .userName("Usern")
                .createdAt(Instant.now())
                .build();
        accountRepo.saveAndFlush(accountEntity);


        AccountEntity accountEntity2 = AccountEntity.builder()
                .anonymousBuyer(false)
                .provider(AuthProvider.google)
                .providerId("2")
                .city("City2")
                .email("name2@example.com")
                .postNr(23456)
                .streetName("Street 2")
                .userName("Usern2")
                .createdAt(Instant.now())
                .build();
        accountRepo.saveAndFlush(accountEntity2);

        AccountEntity accountEntity3 = AccountEntity.builder()
                .anonymousBuyer(false)
                .provider(AuthProvider.google)
                .providerId("3")
                .city("City3")
                .email("name3@example.com")
                .postNr(23456)
                .streetName("Street 3")
                .userName("Usern3")
                .createdAt(Instant.now())
                .build();
        accountRepo.saveAndFlush(accountEntity3);

        userPrincipal1 = UserPrincipal.create(accountEntity);
        userPrincipal2 = UserPrincipal.create(accountEntity2);
        userPrincipal3 = UserPrincipal.create(accountEntity3);

        auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo, imageRepo, categoryRepo, notificationSender);

        CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                .title("Stol")
                .description("En bra stol")
                .startPrice(1000)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        auctionService.createAuction(userPrincipal1, en_bra_stol);
    }

    @Test
    void createBid() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

        Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

        assertEquals(2000, auction.getCurrentBid());
        assertEquals(AuctionState.InProgress, auction.getState());
    }

    @Test
    void createBids() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

            Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

            assertEquals(2000, auction.getCurrentBid());
            assertEquals(AuctionState.InProgress, auction.getState());
        }
        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(2000).bidPrice(3000).build();

            Auction auction = auctionService.createBid(userPrincipal3, createBidRequest);

            assertEquals(3000, auction.getCurrentBid());
            assertEquals(AuctionState.InProgress, auction.getState());
        }
    }

    @Test
    void createBids_FailWrongCurrentPrice() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

            Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

            assertEquals(2000, auction.getCurrentBid());
        }
        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(1900).bidPrice(3000).build();

            assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal3, createBidRequest));

        }
    }

    @Test
    void createBids_FailToLowBid() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

            Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

            assertEquals(2000, auction.getCurrentBid());
        }
        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(2000).bidPrice(2050).build();

            assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal3, createBidRequest));

        }
    }

    @Test
    void createBids_FailSameBidder() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

            Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

            assertEquals(2000, auction.getCurrentBid());
        }
        {
            CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(2000).bidPrice(3000).build();
            assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal2, createBidRequest));

        }
    }

//    @Test
//    void createBid_FailWhenBidderNotFound() {
//        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();
//
//        CreateBidRequest createBidRequest = CreateBidRequest.builder()
//                .auctionId(first.getId())
//                .currentPrice(first.getStartPrice())
//                .bidPrice(2000)
//                .build();
//
//        assertThrows(NoSuchElementException.class, () -> auctionService.createBid("UserAAA2", createBidRequest));
//    }

    @Test
    void createBid_FailWhenBidderSameAsSeller() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(2000).build();

        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal1, createBidRequest));
    }

    @Test
    void createBid_FailWhenWrongCurrentPrice() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder()
                .auctionId(first.getId())
                .currentPrice(10)
                .bidPrice(2000)
                .build();

        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal2, createBidRequest));
    }

    @Test
    void createBid_FailWhenToLowBid() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder()
                .auctionId(first.getId())
                .currentPrice(first.getStartPrice())
                .bidPrice(1049)
                .build();

        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal2, createBidRequest));
    }

    @Test
    void createBid_FailWhenBidPriceGreaterThanCurrent() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder()
                .auctionId(first.getId())
                .currentPrice(2001)
                .bidPrice(2000)
                .build();

        assertThrows(IllegalArgumentException.class, () -> auctionService.createBid(userPrincipal2, createBidRequest));
    }

    @Test
    void createBid_FailWhenWrongAuctionId() {
        Auction first = auctionService.getAuctions(null, null).stream().findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder()
                .auctionId(99)
                .currentPrice(first.getStartPrice())
                .bidPrice(2000)
                .build();

        assertThrows(NoSuchElementException.class, () -> auctionService.createBid(userPrincipal2, createBidRequest));
    }

    @Test
    void createBid_FailWhenBidRequestIsNull() {
        assertThrows(NullPointerException.class, () -> auctionService.createBid(userPrincipal2, null));
    }

    @Test
    void createBid_AndWinBuyout() {
        CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                .title("Stol")
                .description("En bra stol")
                .startPrice(1000)
                .buyoutPrice(4000)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        auctionService.createAuction(userPrincipal1, en_bra_stol);
        Auction first = auctionService.getAuctions(null, null).stream().filter(a -> a.getBuyoutPrice() == 4000).findFirst().get();

        CreateBidRequest createBidRequest = CreateBidRequest.builder().auctionId(first.getId()).currentPrice(first.getStartPrice()).bidPrice(4000).build();

        Auction auction = auctionService.createBid(userPrincipal2, createBidRequest);

        assertEquals(4000, auction.getCurrentBid());
        assertEquals(AuctionState.EndedWithBuyout, auction.getState());
    }
}
