package se.iths.auktionera.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "auction_id",
            nullable = false
    )
    private AuctionEntity auction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "seller_id",
            nullable = false
    )
    private AccountEntity seller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "buyer_id",
            nullable = false
    )
    private AccountEntity buyer;

    private int rating;

    private String text;

    private boolean createdBySeller;

    public ReviewEntity(boolean createdBySeller, int rating, String text, AuctionEntity auction, AccountEntity seller, AccountEntity buyer) {
        Validate.isTrue(rating >= 0 && rating <= 5, "Rating not valid.");
        this.auction = Objects.requireNonNull(auction);
        this.seller = Objects.requireNonNull(seller);
        this.buyer = Objects.requireNonNull(buyer);
        this.rating = rating;
        this.text = text;
        this.createdBySeller = createdBySeller;
    }
}
