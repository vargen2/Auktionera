package se.iths.auktionera.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuctionRequest {

    @NotBlank
    private String description;

    @NotBlank
    @Max(255)
    private String title;

    @NotNull
    private Instant endsAt;

    @NotNull
    @Min(0)
    private Integer startPrice;

    @Min(0)
    private Integer buyoutPrice;

    @NotNull
    @Min(10)
    private Integer minBidStep;


}
