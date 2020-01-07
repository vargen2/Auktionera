package se.iths.auktionera.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Image;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.ImageRepo;

@Service
public class ImageService implements IImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final AccountRepo accountRepo;
    private final ImageRepo imageRepo;

    public ImageService(AccountRepo accountRepo, ImageRepo imageRepo) {
        this.accountRepo = accountRepo;
        this.imageRepo = imageRepo;
    }

    @Override
    public Image createImage(String authId, String url) {
        return null;
//        var creator = accountRepo.findByAuthId(authId).orElseThrow();
//        var imageEntity = new ImageEntity(url, creator);
//        var image = new Image(imageRepo.saveAndFlush(imageEntity));
//        log.info("Image created: {}", image);
//        return image;
    }
}
