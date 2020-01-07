package se.iths.auktionera.persistence.entity;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import se.iths.auktionera.business.enums.AuthProvider;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = true, updatable = false)
    private String authId = "";

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Builder.Default
    private String userName = StringUtils.EMPTY;
    @Builder.Default
    private String email = StringUtils.EMPTY;
    private boolean anonymousBuyer;
    private Instant createdAt;
    @Builder.Default
    private String streetName = StringUtils.EMPTY;
    private int postNr;
    @Builder.Default
    private String city = StringUtils.EMPTY;

    private boolean receiveEmailWhenReviewed;
    private boolean receiveEmailWhenOutbid;
    private boolean receiveEmailWhenWon;
    private boolean receiveEmailWhenSold;

}
