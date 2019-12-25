package se.iths.auktionera.business.service;

import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.model.UserStats;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public List<Review> getUserReviews(long id) {
        return null;
    }

    @Override
    public UserStats getUserStats(long id) {
        return null;
    }
}
