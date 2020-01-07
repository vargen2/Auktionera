package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface IAuctionService {
    List<Auction> getAuctions(Map<String, String> filters, Map<String, String> sorters);

    Auction getAuction(long id);

    Auction createAuction(UserPrincipal userPrincipal, CreateAuctionRequest auctionRequest);

    Auction createBid(String authId, CreateBidRequest bidRequest);

}
