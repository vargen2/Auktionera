package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.persistence.entity.AccountEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String streetName;
    private int postNr;
    private String city;

    public Address(AccountEntity accountEntity) {
        this.streetName = accountEntity.getStreetName();
        this.postNr = accountEntity.getPostNr();
        this.city = accountEntity.getCity();

    }
}
