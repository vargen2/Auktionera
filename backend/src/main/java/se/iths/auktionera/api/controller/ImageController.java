package se.iths.auktionera.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.CreateImageRequest;
import se.iths.auktionera.business.model.Image;
import se.iths.auktionera.business.service.IImageService;
import se.iths.auktionera.security.CurrentUser;
import se.iths.auktionera.security.UserPrincipal;

import javax.validation.Valid;

@RestController
public class ImageController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Image createImage(@Valid @RequestBody CreateImageRequest imageRequest, @CurrentUser UserPrincipal userPrincipal) {
        return imageService.createImage(userPrincipal, imageRequest.getUrl());
    }
}
