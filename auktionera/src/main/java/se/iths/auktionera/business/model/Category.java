package se.iths.auktionera.business.model;


import lombok.Getter;
import lombok.ToString;
import se.iths.auktionera.persistence.entity.CategoryEntity;

@ToString
@Getter
public class Category {
    private long id;
    private String title;

    public Category(CategoryEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
