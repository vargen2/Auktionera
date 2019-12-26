package se.iths.auktionera.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuctionRequest {

    @NotBlank
    private String description;

    @NotBlank
    @Length(max = 255, min = 1)
    private String title;

    @NotNull
    private Instant endsAt;

    @NotNull
    @Min(0)
    private Integer startPrice;

    @Min(0)
    private Integer buyoutPrice = null;

    private List<Long> imageIds;
}
