package se.iths.auktionera.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class CategoryEntity {

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    Set<AuctionEntity> auctions;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NaturalId
    @Column(nullable = false, unique = true)
    private String title;

    public CategoryEntity(String title) {
        this.title = Objects.requireNonNull(title);

    }
}
