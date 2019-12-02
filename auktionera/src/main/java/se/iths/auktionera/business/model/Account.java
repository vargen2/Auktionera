package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.persistence.entity.AccountEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private User user;
    private String email;
    private boolean anonymousBuyer;
    private Address address;

    public Account(AccountEntity ent) {
        this.address = new Address(ent);
        this.user = new User(ent);
        this.email = ent.getEmail();
        this.anonymousBuyer = ent.isAnonymousBuyer();
    }
}
