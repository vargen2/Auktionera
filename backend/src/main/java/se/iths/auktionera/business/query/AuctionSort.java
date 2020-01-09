package se.iths.auktionera.business.query;

import org.springframework.data.domain.Sort;

import java.util.Map;

public class AuctionSort {

    public static Sort create(Map<String, String> sorters) {
        Sort sort = Sort.unsorted();

        if (sorters.containsKey("endsat")) {
            System.out.println(sorters.get("endsat"));
            sort = sort.and(Sort.by(Sort.Direction.fromString(sorters.get("endsat")), "endsAt"));
        }

        if (sorters.containsKey("endedat")) {
            sort = sort.and(Sort.by(Sort.Direction.fromString(sorters.get("endedat")), "endedAt"));
        }

        if (sorters.containsKey("createdat")) {
            sort = sort.and(Sort.by(Sort.Direction.fromString(sorters.get("createdat")), "createdAt"));
        }

        return sort;
    }
}
