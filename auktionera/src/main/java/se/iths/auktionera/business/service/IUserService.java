package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.model.UserStats;

import java.util.List;

public interface IUserService {

    List<User> getUsers();

    User getUser(long id);

    List<Review> getUserReviews(long id);

    UserStats getUserStats(long id);
}
