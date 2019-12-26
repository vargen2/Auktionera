package se.iths.auktionera.business.service;

import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.model.UserStats;
import se.iths.auktionera.persistence.entity.ReviewEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;
import se.iths.auktionera.persistence.repo.BidRepo;
import se.iths.auktionera.persistence.repo.ReviewRepo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final AccountRepo accountRepo;
    private final AuctionRepo auctionRepo;
    private final BidRepo bidRepo;
    private final ReviewRepo reviewRepo;

    public UserService(AccountRepo accountRepo, AuctionRepo auctionRepo, BidRepo bidRepo, ReviewRepo reviewRepo) {
        this.accountRepo = accountRepo;
        this.auctionRepo = auctionRepo;
        this.bidRepo = bidRepo;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public List<User> getUsers() {
        return accountRepo.findAll().stream().map(User::new).collect(Collectors.toList());
    }

    @Override
    public User getUser(long id) {
        return new User(accountRepo.findById(id).orElseThrow());
    }

    @Override
    public List<Review> getUserReviews(long id, boolean saleReviews) {
        if (saleReviews) {
            return reviewRepo.findAllOnSeller(id).stream().map(Review::new).collect(Collectors.toList());
        } else {
            return reviewRepo.findAllOnBuyer(id).stream().map(Review::new).collect(Collectors.toList());
        }
    }

    @Override
    public UserStats getUserStats(long id) {
        return UserStats.builder()
                .totalAuctionsCreated(auctionRepo.countAllBySeller_Id(id))
                .totalAuctionsSold(auctionRepo.countAllSoldBySeller(id))
                .totalBuys(auctionRepo.countAllByBuyer_Id(id))
                .totalBids(bidRepo.countAllByBidderId(id))
                .sellerRating(calcAverageRating(reviewRepo::findAllOnSeller, id))
                .buyerRating(calcAverageRating(reviewRepo::findAllOnBuyer, id))
                .build();
    }

    private float calcAverageRating(Function<Long, List<ReviewEntity>> function, long id) {
        return (float) function.apply(id)
                .stream()
                .mapToDouble(ReviewEntity::getRating)
                .average()
                .orElse(0);
    }
}
