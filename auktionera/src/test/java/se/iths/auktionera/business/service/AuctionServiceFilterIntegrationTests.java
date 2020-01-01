package se.iths.auktionera.business.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ImageRepo;
import se.iths.auktionera.worker.INotificationSender;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class AuctionServiceFilterIntegrationTests {


    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ImageRepo imageRepo;
    @MockBean
    private INotificationSender notificationSender;

    private IAuctionService auctionService;

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


        auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo, imageRepo, notificationSender);

        {
            CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                    .title("Stol")
                    .description("En bra stol")
                    .startPrice(1000)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                    .build();

            auctionService.createAuction("User", en_bra_stol);
        }
        {
            CreateAuctionRequest request = CreateAuctionRequest.builder()
                    .title("Stol")
                    .description("En dålig stol")
                    .startPrice(100)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                    .build();

            auctionService.createAuction("User", request);
        }

        {
            CreateAuctionRequest request = CreateAuctionRequest.builder()
                    .title("Stolen")
                    .description("En mellan stol")
                    .startPrice(500)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                    .build();

            auctionService.createAuction("User", request);
        }

        {
            CreateAuctionRequest request = CreateAuctionRequest.builder()
                    .title("Boat")
                    .description("A plastic boat. No engine")
                    .startPrice(15000)
                    .buyoutPrice(30000)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS))
                    .build();

            auctionService.createAuction("User", request);
        }

        {
            CreateAuctionRequest request = CreateAuctionRequest.builder()
                    .title("Volvo V40")
                    .description("A car. Good condition.")
                    .startPrice(25000)
                    .buyoutPrice(40000)
                    .endsAt(Instant.now().plus(4, ChronoUnit.DAYS))
                    .build();

            auctionService.createAuction("User", request);
        }
    }

    @Test
    void getAuctionsByAnyOfWords() {
        List<Auction> auctions = auctionService.getAuctions(null, null);
        assertEquals(auctionRepo.findAll().size(), auctions.size());
    }

    @Test
    void getAuctionsByTitle() {
        Map<String, String> filters = Map.of("title", "Boat");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Boat", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByDescription() {
        Map<String, String> filters = Map.of("description", "car");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Volvo V40", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByTitleAndDescription() {
        Map<String, String> filters = Map.of("description", "car", "title", "volvo v40");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Volvo V40", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByDescriptions() {
        Map<String, String> filters = Map.of("description", "stol");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(3, auctions.size());
        assertTrue(auctions.stream().allMatch(a -> a.getDescription().contains("stol")));
    }

    @Test
    void getAuctionsByState() {
        Map<String, String> filters = Map.of("state", "InProgress");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(5, auctions.size());
    }

    @Test
    void getAuctionsByStateNone() {
        Map<String, String> filters = Map.of("state", "EndedWithBuyout");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(0, auctions.size());
    }

    @Test
    void getAuctionsByTitleAndState() {
        Map<String, String> filters = Map.of("title", "Boat", "state", "InProgress");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Boat", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByTitleDescriptionAndState() {
        Map<String, String> filters = Map.of("title", "Boat", "description", "boat", "state", "InProgress");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Boat", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByBuyoutLessThan() {
        Map<String, String> filters = Map.of("buyoutlessthan", "31000");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Boat", auctions.get(0).getTitle());
    }


    @Test
    void getAuctionsByTitleDescriptionAndStateBuyoutlessthan() {
        Map<String, String> filters = Map.of("buyoutlessthan", "31000", "title", "Boat", "description", "boat", "state", "InProgress");
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
        assertEquals("Boat", auctions.get(0).getTitle());
    }

    @Test
    void getAuctionsByEndsAtBefore() {
        Map<String, String> filters = Map.of("endsatbefore", Instant.now().plus(2, ChronoUnit.DAYS).toString());
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(4, auctions.size());
    }

    @Test
    void getAuctionsByEndsAtBeforeOneHour() {
        Map<String, String> filters = Map.of("endsatbefore", Instant.now().plus(1, ChronoUnit.HOURS).toString());
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(0, auctions.size());
    }

    @Test
    void getAuctionsByTitleDescriptionEndsAtBefore() {
        Map<String, String> filters = Map.of("description", "car", "title", "Boat", "endsatbefore", Instant.now().plus(5, ChronoUnit.DAYS).toString());
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(2, auctions.size());
    }

    @Test
    void getAuctionsByTitleDescriptionEndsAtBeforeBuyot() {
        Map<String, String> filters = Map.of("buyoutlessthan", "31000", "description", "car", "title", "Boat", "endsatbefore", Instant.now().plus(5, ChronoUnit.DAYS).toString());
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
    }

    @Test
    void getAuctionsByEndsAtAfter() {
        Map<String, String> filters = Map.of("endsatafter", Instant.now().plus(3, ChronoUnit.DAYS).toString());
        List<Auction> auctions = auctionService.getAuctions(filters, null);
        assertEquals(1, auctions.size());
    }

    @Test
    void getAuctionsByEndsAtBeforeSortByEndsAtASC() {
        Map<String, String> filters = Map.of("endsatbefore", Instant.now().plus(2, ChronoUnit.DAYS).toString());
        Map<String, String> sorters = Map.of("endsat", "asc");
        List<Auction> auctions = auctionService.getAuctions(filters, sorters);
        assertEquals(4, auctions.size());
        assertEquals("En dålig stol", auctions.get(0).getDescription());
        assertEquals("En bra stol", auctions.get(1).getDescription());
        assertEquals("En mellan stol", auctions.get(2).getDescription());
        assertEquals("A plastic boat. No engine", auctions.get(3).getDescription());
    }


    @Test
    void getAuctionsByEndsAtBeforeSortByEndsAtDESC() {
        Map<String, String> filters = Map.of("endsatbefore", Instant.now().plus(2, ChronoUnit.DAYS).toString());
        Map<String, String> sorters = Map.of("endsat", "desc");
        List<Auction> auctions = auctionService.getAuctions(filters, sorters);
        assertEquals(4, auctions.size());
        assertEquals("En dålig stol", auctions.get(3).getDescription());
        assertEquals("En bra stol", auctions.get(2).getDescription());
        assertEquals("En mellan stol", auctions.get(1).getDescription());
        assertEquals("A plastic boat. No engine", auctions.get(0).getDescription());
    }

}
