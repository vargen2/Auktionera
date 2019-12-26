package se.iths.auktionera.api.controller;

import org.springframework.web.bind.annotation.*;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.business.service.IAuctionService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AuctionController {

    private final IAuctionService auctionService;

    public AuctionController(IAuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("api/auctions")
    public List<Auction> getAuctions(@RequestParam Map<String, String> all) {
        return auctionService.getAuctions(all, all);
    }

    @GetMapping("api/auctions/{id}")
    public Auction getAuction(@PathVariable long id) {
        return auctionService.getAuction(id);
    }

    @PostMapping("api/auctions")
    public Auction createAuction(@Valid @RequestBody CreateAuctionRequest auctionRequest, HttpServletRequest request) {
        return auctionService.createAuction((String) request.getAttribute("authId"), auctionRequest);
    }

    @PostMapping("api/auctions/bid")
    public Auction createBid(@Valid @RequestBody CreateBidRequest bidRequest, HttpServletRequest request) {
        return auctionService.createBid((String) request.getAttribute("authId"), bidRequest);
    }


}
