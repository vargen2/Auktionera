package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.persistence.entity.AuctionEntity;

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
//            "state" : enum:AuctionState,
//            "endsAt" : Date,
//            "createdAt" : Date,
//            "currentBidAt" : Date,
//            "endedAt" : Date,
//            "startPrice" : 0,
//            "buyoutPrice" : 0,
//            "minBidStep" : 0,
//            "currentBid" : 0,
//            "deliveryType" : enum:DeliveryType


    public Auction(AuctionEntity auctionEntity) {
        this.title = auctionEntity.getTitle();
        this.description = auctionEntity.getDescription();
        this.id = auctionEntity.getId();
        this.seller = new User(auctionEntity.getSeller());
    }
}
