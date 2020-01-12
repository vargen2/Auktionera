package se.iths.auktionera.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

@Getter
@NoArgsConstructor
public class UpdateAccountRequest {

    private String userName = null;

    private String streetName = null;

    private String city = null;

    private Integer postNr = null;

    private Boolean anonymousBuyer = null;

    private Boolean receiveEmailWhenReviewed = null;

    private Boolean receiveEmailWhenOutbid = null;

    public void setUserName(String newUserName) {
        Validate.isTrue(StringUtils.isAlphanumeric(newUserName), "Not valid userName");
        this.userName = newUserName;
    }

}
