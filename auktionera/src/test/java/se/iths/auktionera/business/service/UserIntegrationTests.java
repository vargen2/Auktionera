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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class UserIntegrationTests {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private AuctionRepo auctionRepo;
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ReviewRepo reviewRepo;

    private IUserService userService;

    private long user1Id;
    private long user2Id;
    private long auctionId;
    private long auctionId2;

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
        user1Id = accountEntity.getId();

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
        user2Id = accountEntity2.getId();

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


        IAuctionService auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo);
        IReviewService reviewService = new ReviewService(accountRepo, auctionRepo, bidRepo, reviewRepo);
        userService = new UserService(accountRepo, auctionRepo, bidRepo, reviewRepo);

        {
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

        {
            CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                    .title("Stol2")
                    .description("En bra stol2")
                    .startPrice(2000)
                    .buyoutPrice(4000)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                    .build();

            Auction auction = auctionService.createAuction("User", en_bra_stol);
            auctionId2 = auction.getId();
            CreateBidRequest bidRequest = CreateBidRequest.builder().bidPrice(4000).currentPrice(2000).auctionId(auction.getId()).build();
            auctionService.createBid("User3", bidRequest);
        }

        {
            CreateAuctionRequest en_bra_stol = CreateAuctionRequest.builder()
                    .title("Stol3")
                    .description("En bra stol3")
                    .startPrice(2000)
                    .buyoutPrice(4000)
                    .endsAt(Instant.now().plus(1, ChronoUnit.DAYS))
                    .build();

            Auction auction = auctionService.createAuction("User", en_bra_stol);


        }

        {
            CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(5).text("Product was great.").build();
            reviewService.createReview("User2", reviewRequest);
        }
        {
            CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId).rating(2).text("I don't like buyer.").build();
            reviewService.createReview("User", reviewRequest);
        }

        {
            CreateReviewRequest reviewRequest = CreateReviewRequest.builder().auctionId(auctionId2).rating(3).text("Product was medium.").build();
            reviewService.createReview("User3", reviewRequest);
        }
    }

    @Test
    public void getUsers() {
        List<User> users = userService.getUsers();
        assertEquals(3, users.size());
    }

    @Test
    public void getUser() {
        User user = userService.getUser(user1Id);
        assertEquals(user1Id, user.getId());
    }

    @Test
    public void getUserSaleReviews() {
        {
            List<Review> reviews = userService.getUserReviews(user1Id, true);
            assertEquals(2, reviews.size());
            assertEquals(5, reviews.get(0).getRating());
        }
        {
            List<Review> reviews = userService.getUserReviews(user1Id, false);
            assertEquals(0, reviews.size());
        }
    }

    @Test
    public void getUserBuyReviews() {
        {
            List<Review> reviews = userService.getUserReviews(user2Id, false);
            assertEquals(2, reviews.get(0).getRating());
        }
        {
            List<Review> reviews = userService.getUserReviews(user2Id, true);
            assertEquals(0, reviews.size());
        }
    }

    @Test
    public void getUserStats() {
        {
            UserStats userStats = userService.getUserStats(user1Id);
            assertEquals(3, userStats.getTotalAuctionsCreated());
            assertEquals(2, userStats.getTotalAuctionsSold());
            assertEquals(0, userStats.getTotalBids());
            assertEquals(4, userStats.getSellerRating());
            assertEquals(0, userStats.getBuyerRating());
            assertEquals(0, userStats.getTotalBuys());
        }
        {
            UserStats userStats = userService.getUserStats(user2Id);
            assertEquals(0, userStats.getTotalAuctionsCreated());
            assertEquals(0, userStats.getTotalAuctionsSold());
            assertEquals(1, userStats.getTotalBids());
            assertEquals(0, userStats.getSellerRating());
            assertEquals(2, userStats.getBuyerRating());
            assertEquals(1, userStats.getTotalBuys());
        }
    }
}
