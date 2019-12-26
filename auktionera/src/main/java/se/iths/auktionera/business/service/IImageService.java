package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Image;

public interface IImageService {
    Image createImage(String authId, String url);
}
