package se.iths.auktionera.business.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReviewRequest {

    @Min(1)
    private long auctionId;

    @Min(0)
    @Max(5)
    private int rating;

    @Length(max = 1024)
    private String text;
}
