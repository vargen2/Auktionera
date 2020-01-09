package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.entity.BidEntity;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private Set<Category> categories;
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
        this.currentBid = auctionEntity.getCurrentBid();
        this.categories = auctionEntity.getCategories() == null ? Set.of() : auctionEntity.getCategories().stream().map(Category::new).collect(Collectors.toSet());
    }

    public Auction(AuctionEntity auctionEntity, BidEntity bidEntity) {
        this(auctionEntity);
        this.currentBid = bidEntity.getBidPrice();
        this.currentBidAt = bidEntity.getBidAt();
    }
}
