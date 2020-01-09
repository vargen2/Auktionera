package se.iths.auktionera.persistence.entity;

import lombok.*;
import org.apache.commons.lang3.Validate;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.business.model.CreateAuctionRequest;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "seller_id",
            nullable = false
    )
    private AccountEntity seller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "buyer_id"
    )
    private AccountEntity buyer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "categories",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<CategoryEntity> categories = new HashSet<>();

    private String title;
    private String description;
    private AuctionState state;
    private Instant endsAt;
    private Instant createdAt;
    private Instant endedAt;
    private int startPrice;
    private int buyoutPrice;
    private int currentBid;


    public AuctionEntity(CreateAuctionRequest request, AccountEntity seller) {
        Objects.requireNonNull(request);
        this.seller = Objects.requireNonNull(seller);
        this.state = AuctionState.InProgress;
        this.createdAt = Instant.now();
        this.endsAt = request.getEndsAt();
        this.startPrice = request.getStartPrice();
        Optional.ofNullable(request.getBuyoutPrice()).ifPresent(this::setBuyoutPrice);
        this.title = request.getTitle();
        this.description = request.getDescription();

        if (buyoutPrice > 0) {
            Validate.isTrue(buyoutPrice > startPrice, "Not valid buyout");
        }
        Validate.isTrue(endsAt.isAfter(createdAt.plus(4, ChronoUnit.HOURS)), "Not valid end date, minimum 4 hours.");

    }
}
