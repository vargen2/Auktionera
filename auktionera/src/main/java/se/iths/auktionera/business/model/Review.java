package se.iths.auktionera.business.model;

import lombok.Getter;
import se.iths.auktionera.persistence.entity.ReviewEntity;

@Getter
public class Review {

    private long id;

    private int rating;

    private String text;

    public Review(ReviewEntity reviewEntity) {
        this.id = reviewEntity.getId();
        this.rating = reviewEntity.getRating();
        this.text = reviewEntity.getText();
    }
}
