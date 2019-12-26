package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    private long id;
    private String title;
    private String description;
    private User seller;
    private User buyer;
    private AuctionState state;
    private Instant endsAt;
    private Instant createdAt;
    private Instant currentBidAt;
    private Instant endedAt;
    private int startPrice;
    private int buyoutPrice;
    private int currentBid;
    // "deliveryType" : enum:DeliveryType

    public Auction(AuctionEntity auctionEntity) {
        this.title = auctionEntity.getTitle();
        this.description = auctionEntity.getDescription();
        this.id = auctionEntity.getId();
        this.seller = new User(auctionEntity.getSeller());
        this.buyer = auctionEntity.getBuyer() != null ? new User(auctionEntity.getBuyer()) : null;
        this.createdAt = auctionEntity.getCreatedAt();
        this.buyoutPrice = auctionEntity.getBuyoutPrice();
        this.startPrice = auctionEntity.getStartPrice();
        this.endsAt = auctionEntity.getEndsAt();
        this.state = auctionEntity.getState();
    }

    public Auction(AuctionEntity auctionEntity, BidEntity bidEntity) {
        this.title = auctionEntity.getTitle();
        this.description = auctionEntity.getDescription();
        this.id = auctionEntity.getId();
        this.seller = new User(auctionEntity.getSeller());
        this.buyer = auctionEntity.getBuyer() != null ? new User(auctionEntity.getBuyer()) : null;
        this.createdAt = auctionEntity.getCreatedAt();
        this.buyoutPrice = auctionEntity.getBuyoutPrice();
        this.startPrice = auctionEntity.getStartPrice();
        this.endsAt = auctionEntity.getEndsAt();
        this.state = auctionEntity.getState();

        this.currentBid = bidEntity.getBidPrice();
        this.currentBidAt = bidEntity.getBidAt();
    }
}
