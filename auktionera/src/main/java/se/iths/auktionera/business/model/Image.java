package se.iths.auktionera.business.model;

import lombok.Getter;
import se.iths.auktionera.persistence.entity.ImageEntity;

@Getter
public class Image {
    private long id;
    private String url;

    public Image(ImageEntity entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
    }
}
