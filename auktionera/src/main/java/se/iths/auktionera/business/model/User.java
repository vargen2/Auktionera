package se.iths.auktionera.business.model;

import lombok.*;
import se.iths.auktionera.persistence.entity.AccountEntity;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String userName;
    private Instant createdAt;

    public User(AccountEntity accountEntity) {
        this.id = accountEntity.getId();
        this.userName = accountEntity.getUserName();
        this.createdAt = accountEntity.getCreatedAt();
    }
}
