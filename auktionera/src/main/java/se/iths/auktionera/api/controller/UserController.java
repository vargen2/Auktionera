package se.iths.auktionera.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.enums.ReviewType;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.model.User;
import se.iths.auktionera.business.model.UserStats;
import se.iths.auktionera.business.service.IUserService;

import java.util.List;

@RestController
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("api/users/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping("api/users/{id}/reviews/{type}")
    public List<Review> getUserReviews(@PathVariable long id, @PathVariable ReviewType type) {
        return userService.getUserReviews(id, type == ReviewType.sales);
    }

    @GetMapping("api/users/{id}/stats")
    public UserStats getUserStats(@PathVariable long id) {
        return userService.getUserStats(id);
    }
}
