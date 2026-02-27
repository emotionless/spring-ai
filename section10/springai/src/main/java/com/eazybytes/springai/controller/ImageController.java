package com.eazybytes.springai.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/image")
    public String generateImage(@RequestParam("message") String message) {
        var imageResponse = imageModel.call(
                new ImagePrompt(message)
        );
        return Objects.requireNonNull(imageResponse.getResult()).getOutput().getUrl();
    }

    @GetMapping("/image-options")
    public String generateImageWithOptions(@RequestParam("message") String message) {
        var imageResponse = imageModel.call(
                new ImagePrompt(message, OpenAiImageOptions.builder()
                        .N(1)
                        .quality("hd")
                        .style("natural")
                        .height(1024)
                        .width(1024)
                        .responseFormat("url")
                        .build())
        );
        return Objects.requireNonNull(imageResponse.getResult()).getOutput().getUrl();
    }
}
