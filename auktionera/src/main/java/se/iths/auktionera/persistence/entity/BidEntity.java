package se.iths.auktionera.persistence.entity;

import lombok.*;
import se.iths.auktionera.business.model.CreateBidRequest;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"auction_id", "bidder_id", "previousBidId"}))
public class BidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "bidder_id",
            nullable = false
    )
    private AccountEntity bidder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "auction_id",
            nullable = false
    )
    private AuctionEntity auction;

    private Instant bidAt;
    private int bidPrice;
    private long previousBidId;


    public BidEntity(long previousBidId, CreateBidRequest bidRequest, AuctionEntity auction, AccountEntity bidder) {
        Objects.requireNonNull(bidRequest);
        this.bidder = Objects.requireNonNull(bidder);
        this.auction = auction;

        this.bidAt = Instant.now();
        this.bidPrice = bidRequest.getBidPrice();
        this.previousBidId = previousBidId;
    }
}
