package se.iths.auktionera.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.iths.auktionera.business.model.CreateImageRequest;
import se.iths.auktionera.business.model.Image;
import se.iths.auktionera.business.service.IImageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class ImageController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public Image createImage(@Valid @RequestBody CreateImageRequest imageRequest, HttpServletRequest request) {
        return imageService.createImage((String) request.getAttribute("authId"), imageRequest.getUrl());
    }
}
