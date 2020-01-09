package se.iths.auktionera.business.service;

import se.iths.auktionera.business.model.Image;
import se.iths.auktionera.security.UserPrincipal;

public interface IImageService {
    Image createImage(UserPrincipal userPrincipal, String url);
}
