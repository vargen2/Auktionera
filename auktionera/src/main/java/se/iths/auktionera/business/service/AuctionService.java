package se.iths.auktionera.business.service;

import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Auction;
import se.iths.auktionera.business.model.CreateAuctionRequest;
import se.iths.auktionera.persistence.entity.AuctionEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.AuctionRepo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuctionService implements IAuctionService {

    private final AccountRepo accountRepo;
    private final AuctionRepo auctionRepo;

    public AuctionService(AccountRepo accountRepo, AuctionRepo auctionRepo) {
        this.accountRepo = accountRepo;
        this.auctionRepo = auctionRepo;
    }

    @Override
    public List<Auction> getAuctions(Map<String, String> filters, Map<String, String> sorters) {
        return auctionRepo.findAll().stream().map(Auction::new).collect(Collectors.toList());
    }

    @Override
    public Auction getAuction(long id) {
        return new Auction(auctionRepo.findById(id).orElseThrow());
    }

    @Override
    public Auction createAuction(String authId, CreateAuctionRequest auctionRequest) {
        var seller = Objects.requireNonNull(accountRepo.findByAuthId(authId));
        var auctionEntity = new AuctionEntity(auctionRequest, seller);

        return new Auction(auctionRepo.saveAndFlush(auctionEntity));
    }
}
