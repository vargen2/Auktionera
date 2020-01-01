package se.iths.auktionera.business.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import se.iths.auktionera.business.model.*;
import se.iths.auktionera.persistence.entity.AccountEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ImageRepo;
import se.iths.auktionera.worker.INotificationSender;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ImageIntegrationTests {

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

    private IImageService imageService;

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


        IAuctionService auctionService = new AuctionService(accountRepo, auctionRepo, bidRepo, imageRepo, notificationSender);

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

        imageService = new ImageService(accountRepo, imageRepo);
    }

    @Test
    public void createImage() {
        CreateImageRequest imageRequest = CreateImageRequest.builder().url("www.example.com").build();
        Image image = imageService.createImage("User", imageRequest.getUrl());
        assertEquals(imageRequest.getUrl(), image.getUrl());
    }
}
