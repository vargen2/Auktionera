package se.iths.auktionera.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@AllArgsConstructor
public class EmailNotification implements Serializable {
    private String email;
    private String message;
}
