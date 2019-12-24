package se.iths.auktionera.business.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.auktionera.business.model.*;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ReviewRepo;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewIntegrationTests {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ReviewRepo reviewRepo;

    private IAuctionService auctionService;

    private long auctionId;

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


        AccountEntity accountEntity2 = AccountEntity.builder()
                .anonymousBuyer(false)
                .authId("User2")
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
                .authId("User3")
                .city("City3")
                .email("name3@example.com")
                .postNr(23456)
                .streetName("Street 3")
                .userName("Usern3")
                .createdAt(Instant.now())
                .build();
        accountRepo.saveAndFlush(accountEntity3);


        auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo, reviewRepo);

        CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                .title("Stol")
                .description("En bra stol")
                .startPrice(1000)
                .buyoutPrice(3000)
                .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        Auction auction = auctionService.createAuction("User", en_bra_stol);
        auctionId = auction.getId();
        CreateBidRequest bidRequest = CreateBidRequest.builder().bidPrice(3000).currentPrice(1000).auctionId(auctionId).build();

        auctionService.createBid("User2", bidRequest);
    }

    @Test
    public void SellerCreate() {
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(5).text("asd").build();
        Review review = auctionService.createReview("User", reviewRequest);
        assertEquals(reviewRequest.getRating(), review.getRating());
        assertEquals(reviewRequest.getText(), review.getText());
    }

    @Test
    public void SellerCreateWithNoText() {
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(5).build();
        Review review = auctionService.createReview("User", reviewRequest);
        assertEquals(reviewRequest.getRating(), review.getRating());
        assertNull(review.getText());
    }

    @Test
    public void SellerCreateAgainFail() {
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(5).text("asd").build();
        Review review = auctionService.createReview("User", reviewRequest);
        assertThrows(IllegalArgumentException.class, () -> auctionService.createReview("User", reviewRequest));
    }

    @Test
    public void BuyerAndSellerCreate() {
        CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(5).text("asd").build();
        {
            Review review = auctionService.createReview("User2", reviewRequest);
            assertEquals(reviewRequest.getRating(), review.getRating());
            assertEquals(reviewRequest.getText(), review.getText());
        }
        {
            Review review = auctionService.createReview("User", reviewRequest);
            assertEquals(reviewRequest.getRating(), review.getRating());
            assertEquals(reviewRequest.getText(), review.getText());
        }
    }
}
