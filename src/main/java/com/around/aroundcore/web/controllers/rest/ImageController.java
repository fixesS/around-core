package com.around.aroundcore.web.controllers.rest;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.database.services.SessionService;
import com.around.aroundcore.web.services.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AroundConfig.API_V1_IMAGE)
@Tag(name="Image controller", description="Handles images requests(saving and loading images).")
public class ImageController {
    private final ImageService imageService;
    private final SessionService sessionService;

    @GetMapping( value = "/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        byte[] image = imageService.loadImage(filename);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<String> updateImage(@RequestParam MultipartFile file) {
        var sessionUuid = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var session = sessionService.findByUuid(sessionUuid);
        var user = session.getUser();
        String imageFileName = imageService.saveImage(file);
        user.setAvatar(imageFileName);
        return ResponseEntity.ok("");
    }
}