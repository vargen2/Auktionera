package se.iths.auktionera.business.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class CreateImageRequest {

    @NotBlank
    @URL
    private String url;

}
