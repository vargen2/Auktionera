package se.iths.auktionera.business.service;

import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.Image;
import se.iths.auktionera.persistence.entity.ImageEntity;
import se.iths.auktionera.persistence.repo.AccountRepo;
import se.iths.auktionera.persistence.repo.ImageRepo;

@Service
public class ImageService implements IImageService {

    private final AccountRepo accountRepo;
    private final ImageRepo imageRepo;

    public ImageService(AccountRepo accountRepo, ImageRepo imageRepo) {
        this.accountRepo = accountRepo;
        this.imageRepo = imageRepo;
    }

    @Override
    public Image createImage(String authId, String url) {
        var creator = accountRepo.findByAuthId(authId).orElseThrow();
        var imageEntity = new ImageEntity(url, creator);
        return new Image(imageRepo.saveAndFlush(imageEntity));
    }
}
