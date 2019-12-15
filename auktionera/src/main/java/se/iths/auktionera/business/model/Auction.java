package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    private long id;
    //"tags" : ["string"],
    private String title;
    private String description;
    private User seller;
    //            "buyer" : User,
//            "sellerReview" : Review,
//            "buyerReview" : Review,
    private AuctionState state;
    private Instant endsAt;
    private Instant createdAt;
    private Instant currentBidAt;
    private Instant endedAt;
    private int startPrice;
    private int buyoutPrice;
    private int minBidStep;
    private int currentBid;
//            "deliveryType" : enum:DeliveryType


    public Auction(AuctionEntity auctionEntity) {
        this.title = auctionEntity.getTitle();
        this.description = auctionEntity.getDescription();
        this.id = auctionEntity.getId();
        this.seller = new User(auctionEntity.getSeller());
        this.createdAt = auctionEntity.getCreatedAt();
        this.buyoutPrice = auctionEntity.getBuyoutPrice();
        this.startPrice = auctionEntity.getStartPrice();
        this.minBidStep = auctionEntity.getMinBidStep();
        this.endsAt = auctionEntity.getEndsAt();
        this.state = auctionEntity.getState();
        this.currentBid = auctionEntity.getCurrentBid();
        this.currentBidAt = auctionEntity.getCurrentBidAt();
    }
}
