package se.iths.auktionera.business.query;

import org.springframework.data.jpa.domain.Specification;
import se.iths.auktionera.business.enums.AuctionState;
import se.iths.auktionera.persistence.entity.AuctionEntity;

import java.time.Instant;
import java.util.Map;

public class AuctionSpecification {

    private static Specification<AuctionEntity> titleContains(String title) {
        return (auction, cq, cb) -> cb.like(auction.get("title"), "%" + title + "%");
    }

    private static Specification<AuctionEntity> descriptionContains(String description) {
        return (auction, cq, cb) -> cb.like(auction.get("description"), "%" + description + "%");
    }

    private static Specification<AuctionEntity> stateIs(AuctionState state) {
        return (auction, cq, cb) -> cb.equal(auction.get("state"), state);
    }

    private static Specification<AuctionEntity> buyoutLessThan(int price) {
        return (auction, cq, cb) -> cb.between(auction.get("buyoutPrice"), 1, price);
    }

    private static Specification<AuctionEntity> endsAtBefore(Instant endsAt) {
        return (auction, cq, cb) -> cb.lessThan(auction.get("endsAt"), endsAt);
    }

    private static Specification<AuctionEntity> endsAtAfter(Instant endsAt) {
        return (auction, cq, cb) -> cb.greaterThan(auction.get("endsAt"), endsAt);
    }

    public static Specification<AuctionEntity> create(Map<String, String> filters) {
        Specification<AuctionEntity> specification = Specification.where(null);
        if (filters.containsKey("title")) {
            specification = specification.or(titleContains(filters.get("title").trim()));
        }

        if (filters.containsKey("description")) {
            specification = specification.or(descriptionContains(filters.get("description").trim()));
        }

        if (filters.containsKey("state")) {
            specification = specification.and(stateIs(AuctionState.valueOf(filters.get("state").trim())));
        }

        if (filters.containsKey("buyoutlessthan")) {
            specification = specification.and(buyoutLessThan(Integer.parseInt(filters.get("buyoutlessthan").trim())));
        }

        if (filters.containsKey("endsatbefore")) {
            specification = specification.and(endsAtBefore(Instant.parse(filters.get("endsatbefore").trim())));
        }

        if (filters.containsKey("endsatafter")) {
            specification = specification.and(endsAtAfter(Instant.parse(filters.get("endsatafter").trim())));
        }

        return specification;
    }
}
