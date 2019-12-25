package se.iths.auktionera.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.CreateReviewRequest;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.business.service.IReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("api/reviews")
    public Review createReview(@Valid @RequestBody CreateReviewRequest reviewRequest, HttpServletRequest request) {
        return reviewService.createReview((String) request.getAttribute("authId"), reviewRequest);
    }
}
