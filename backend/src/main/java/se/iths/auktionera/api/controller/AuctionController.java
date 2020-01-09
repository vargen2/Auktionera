package se.iths.auktionera.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.business.model.CreateBidRequest;
import se.iths.auktionera.business.service.IAuctionService;
import se.iths.auktionera.security.CurrentUser;
import se.iths.auktionera.security.UserPrincipal;

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
    @PreAuthorize("hasRole('USER')")
    public Auction createAuction(@Valid @RequestBody CreateAuctionRequest auctionRequest, @CurrentUser UserPrincipal userPrincipal) {
        return auctionService.createAuction(userPrincipal, auctionRequest);
    }

    @PostMapping("api/auctions/bid")
    @PreAuthorize("hasRole('USER')")
    public Auction createBid(@Valid @RequestBody CreateBidRequest bidRequest, @CurrentUser UserPrincipal userPrincipal) {
        return auctionService.createBid(userPrincipal, bidRequest);
    }


}
