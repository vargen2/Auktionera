package se.iths.auktionera.business.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.model.CreateReviewRequest;
import se.iths.auktionera.business.model.EmailNotification;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.persistence.entity.BidEntity;
import se.iths.auktionera.persistence.entity.ReviewEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ReviewRepo;
import se.iths.auktionera.worker.INotificationSender;

import java.util.Objects;

@Service
public class ReviewService implements IReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final AccountRepo accountRepo;
    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;
    private final ReviewRepo reviewRepo;
    private final INotificationSender notificationSender;

    public ReviewService(AccountRepo accountRepo, AuctionRepo auctionRepo, BidRepo bidRepo, ReviewRepo reviewRepo, INotificationSender notificationSender) {
        this.accountRepo = accountRepo;
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
        this.reviewRepo = reviewRepo;
        this.notificationSender = notificationSender;
    }

    @Override
    public Review createReview(String authId, CreateReviewRequest reviewRequest) {
        Objects.requireNonNull(reviewRequest);

        var creator = accountRepo.findByAuthId(authId).orElseThrow();
        var auctionEntity = auctionRepo.findById(reviewRequest.getAuctionId()).orElseThrow();

        Validate.isTrue(auctionEntity.getState() == AuctionState.EndedBought || auctionEntity.getState() == AuctionState.EndedWithBuyout, "Auction not bought.");

        var previousBids = bidRepo.findAllByAuctionIdOrderByBidAt(auctionEntity.getId());
        BidEntity lastBid = previousBids.get(previousBids.size() - 1);
        var buyer = lastBid.getBidder();

        Validate.isTrue(creator.getId() == auctionEntity.getSeller().getId() || creator.getId() == buyer.getId(), "Only seller or buyer can create review.");

        //seller creates review
        if (creator.getId() == auctionEntity.getSeller().getId()) {
            Validate.isTrue(!reviewRepo.existsByAuction_IdAndSeller_IdAndCreatedBySellerTrue(auctionEntity.getId(), auctionEntity.getSeller().getId()), "Review from seller already exists.");
            var reviewEntity = new ReviewEntity(true, reviewRequest.getRating(), reviewRequest.getText(), auctionEntity, auctionEntity.getSeller(), buyer);

            reviewRepo.saveAndFlush(reviewEntity);
            if (buyer.isReceiveEmailWhenReviewed()) {
                notificationSender.enqueueEmailNotification(new EmailNotification(buyer.getEmail(), "New review " + reviewEntity.getId()));
            }
            var review = new Review(reviewEntity);
            log.info("Review created: {}", review);
            return review;
        }

        //buyer creates review
        if (creator.getId() == buyer.getId()) {
            Validate.isTrue(!reviewRepo.existsByAuction_IdAndBuyer_IdAndCreatedBySellerFalse(auctionEntity.getId(), buyer.getId()), "Review from buyer already exists.");
            var reviewEntity = new ReviewEntity(false, reviewRequest.getRating(), reviewRequest.getText(), auctionEntity, auctionEntity.getSeller(), buyer);
            reviewRepo.saveAndFlush(reviewEntity);
            if (auctionEntity.getSeller().isReceiveEmailWhenReviewed()) {
                notificationSender.enqueueEmailNotification(new EmailNotification(auctionEntity.getSeller().getEmail(), "New review " + reviewEntity.getId()));
            }
            var review = new Review(reviewEntity);
            log.info("Review created: {}", review);
            return review;
        }

        return null;
    }
}
