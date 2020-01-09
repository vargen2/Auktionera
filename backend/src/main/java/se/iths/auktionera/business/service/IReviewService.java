package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.CreateReviewRequest;
import se.iths.auktionera.business.model.Review;
import se.iths.auktionera.security.UserPrincipal;

public interface IReviewService {

    Review createReview(UserPrincipal userPrincipal, CreateReviewRequest reviewRequest);
}
