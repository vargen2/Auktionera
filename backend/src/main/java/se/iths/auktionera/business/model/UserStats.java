package se.iths.auktionera.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStats {
    private int totalAuctionsCreated;
    private int totalAuctionsSold;
    private int totalBids;
    private int totalBuys;
    private float sellerRating;
    private float buyerRating;
}
