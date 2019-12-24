package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.*;

import java.util.List;
import java.util.Map;

public interface IAuctionService {
    List<Auction> getAuctions(Map<String, String> filters, Map<String, String> sorters);

    Auction getAuction(long id);

    Auction createAuction(String authId, CreateAuctionRequest auctionRequest);

    Auction createBid(String authId, CreateBidRequest bidRequest);

    Review createReview(String authId, CreateReviewRequest reviewRequest);

}
