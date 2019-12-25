package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.CreateReviewRequest;
import se.iths.auktionera.business.model.Review;

public interface IReviewService {

    Review createReview(String authId, CreateReviewRequest reviewRequest);
}
