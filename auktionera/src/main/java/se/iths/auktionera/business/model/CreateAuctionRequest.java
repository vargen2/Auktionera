package se.iths.auktionera.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuctionRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String title;

}
