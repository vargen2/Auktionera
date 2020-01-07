//package se.iths.auktionera.business.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Sort;
//import se.iths.auktionera.business.model.Auction;
//import se.iths.auktionera.business.model.CreateAuctionRequest;
//import se.iths.auktionera.persistence.entity.AccountEntity;
//import se.iths.auktionera.persistence.entity.AuctionEntity;
//import se.iths.auktionera.persistence.repo.*;
//import se.iths.auktionera.worker.INotificationSender;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@DataJpaTest
//class AuctionServiceTest {
//
//    @MockBean
//    AccountRepo accountRepo;
//    @MockBean
//    AuctionRepo auctionRepo;
//    @MockBean
//    BidRepo bidRepo;
//    @MockBean
//    ReviewRepo reviewRepo;
//    @Autowired
//    private ImageRepo imageRepo;
//    @Autowired
//    private CategoryRepo categoryRepo;
//    @MockBean
//    private INotificationSender notificationSender;
//
//    private IAuctionService auctionService;
//    private AccountEntity accountEntity;
//    private AuctionEntity auctionEntity;
//
//    @BeforeEach
//    void setUp() {
//        auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo, imageRepo, categoryRepo, notificationSender);
//        accountEntity = AccountEntity.builder()
//                .id(10)
//                .anonymousBuyer(false)
//                .authId("User")
//                .city("City")
//                .email("name@example.com")
//                .postNr(12345)
//                .streetName("Street 1")
//                .userName("Usern")
//                .createdAt(Instant.now())
//                .build();
//
//        auctionEntity = AuctionEntity.builder()
//                .id(1000)
//                .title("Stol")
//                .description("En bra stol")
//                .buyoutPrice(20000)
//                .startPrice(100)
//                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
//                .seller(accountEntity)
//                .build();
//    }
//
//    @Test
//    void getAuctions() {
//        when(auctionRepo.findAll(any(Sort.class))).thenReturn(List.of(auctionEntity));
//        List<Auction> auctions = auctionService.getAuctions(null, null);
//        assertNotNull(auctions);
//        assertEquals(1, auctions.size());
//        assertEquals(auctionEntity.getTitle(), auctions.get(0).getTitle());
//    }
//
//    @Test
//    void getAuction() {
//        when(auctionRepo.findById(1000L)).thenReturn(Optional.of(auctionEntity));
//        Auction auction = auctionService.getAuction(1000);
//        assertNotNull(auction);
//
//        assertEquals(auctionEntity.getTitle(), auction.getTitle());
//    }
//
//    @Test
//    void createAuction() {
//        when(accountRepo.findByAuthId("User")).thenReturn(Optional.of(accountEntity));
//        when(auctionRepo.saveAndFlush(any(AuctionEntity.class))).thenReturn(auctionEntity);
//        CreateAuctionRequest auctionRequest = CreateAuctionRequest.builder()
//                .title(auctionEntity.getTitle())
//                .description(auctionEntity.getDescription())
//                .buyoutPrice(auctionEntity.getBuyoutPrice())
//                .endsAt(auctionEntity.getEndsAt())
//                .startPrice(auctionEntity.getStartPrice())
//                .build();
//        Auction auction = auctionService.createAuction("User", auctionRequest);
//        assertNotNull(auction);
//
//        assertEquals(auctionEntity.getTitle(), auction.getTitle());
//        assertEquals(auctionEntity.getDescription(), auction.getDescription());
//        assertEquals(auctionEntity.getBuyoutPrice(), auction.getBuyoutPrice());
//        assertEquals(auctionEntity.getEndsAt(), auction.getEndsAt());
//        assertEquals(auctionEntity.getStartPrice(), auction.getStartPrice());
//        assertEquals(auctionEntity.getSeller().getUserName(), auction.getSeller().getUserName());
//    }
//
//}