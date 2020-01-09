package se.iths.auktionera.business.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
@Builder
public class CreateBidRequest {

    @Min(1)
    private int currentPrice;

    @Min(1)
    private int bidPrice;

    @Min(1)
    private long auctionId;
}
