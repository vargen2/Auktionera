package se.iths.auktionera.persistence.entity;

import lombok.*;

import javax.persistence.*;

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


    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "account_entity_id",
            nullable = false
    )
    private AccountEntity seller;


}
