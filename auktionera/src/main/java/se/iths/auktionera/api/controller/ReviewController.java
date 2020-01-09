package se.iths.auktionera.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.CreateReviewRequest;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.service.IReviewService;
import se.iths.auktionera.security.CurrentUser;
import se.iths.auktionera.security.UserPrincipal;

import javax.validation.Valid;

@RestController
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("api/reviews")
    @PreAuthorize("hasRole('USER')")
    public Review createReview(@Valid @RequestBody CreateReviewRequest reviewRequest, @CurrentUser UserPrincipal userPrincipal) {
        return reviewService.createReview(userPrincipal, reviewRequest);
    }
}
