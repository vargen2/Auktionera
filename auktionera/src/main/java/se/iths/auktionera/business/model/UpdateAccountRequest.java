package se.iths.auktionera.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
public class UpdateAccountRequest {

    private String userName = null;

    @Email
    private String email = null;

    private String streetName = null;

    private String city = null;

    private Integer postNr = null;

    private Boolean anonymousBuyer = null;

    public void setUserName(String newUserName) {
        Validate.isTrue(StringUtils.isAlphanumeric(newUserName), "Not valid userName");
        this.userName = newUserName;
    }

}
