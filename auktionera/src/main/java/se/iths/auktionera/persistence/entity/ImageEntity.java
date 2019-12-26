package se.iths.auktionera.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "creator_id",
            nullable = false
    )
    private AccountEntity creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "auction_id"
    )
    private AuctionEntity auction;

    private String url;

    public ImageEntity(String url, AccountEntity creator) {
        this.url = Objects.requireNonNull(url);
        this.creator = Objects.requireNonNull(creator);
    }
}
