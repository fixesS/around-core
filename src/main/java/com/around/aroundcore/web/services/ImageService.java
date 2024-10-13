package com.around.aroundcore.web.services;

import com.around.aroundcore.web.exceptions.image.*;
import com.around.aroundcore.web.image.ImageType;
import com.around.aroundcore.web.image.MultipartImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    @Value("${around.image.limit}")
    Integer imageLimitSize;
    @Value("${around.image.avatars.directory}")
    String imageAvatarDirectory;
    @Value("${around.image.icons.directory}")
    String imageIconDirectory;
    @Value("${around.image.default.directory}")
    String imageDefaultDirectory;

    public String saveImage(MultipartFile imageFile, ImageType imageType) throws ImageSaveException, ImageSizeException, ImageEmptyException {
        if(imageFile.getSize() > FileUtils.ONE_MB*imageLimitSize){
            throw new ImageSizeException();
        }
        if(imageFile.isEmpty()){
            throw new ImageEmptyException();
        }
        log.info(imageFile.getContentType());
        if(!imageFile.getContentType().equals("image/jpeg") && !imageFile.getContentType().equals("image/png")){
            throw new ImageTypeException();
        }
        try{
            imageFile = convertToJpeg(imageFile);
        }catch (IllegalStateException | IOException e){
            throw new ImageConvertException();
        }

        String uniqueFileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
        Path uploadPath;
        switch (imageType){
            case AVATAR -> uploadPath = Path.of(imageAvatarDirectory);
            case ICON -> uploadPath = Path.of(imageIconDirectory);
            default -> uploadPath = Path.of(imageDefaultDirectory);
        }
        Path filePath = uploadPath.resolve(uniqueFileName);

        try{
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            imageFile.transferTo(filePath);
        } catch (IOException e) {
            throw new ImageSaveException();
        }

        return uniqueFileName;
    }

    public byte[] loadImage(String imageName, ImageType imageType){
        Path imagePath;
        switch (imageType){
            case AVATAR -> imagePath = Path.of(imageAvatarDirectory, imageName);
            case ICON -> imagePath = Path.of(imageIconDirectory, imageName);
            default -> imagePath = Path.of(imageDefaultDirectory, imageName);
        }

        try{
            if (Files.exists(imagePath)) {
                return Files.readAllBytes(imagePath);
            } else {
                throw new ImageLoadException();
            }
        }catch (IOException e) {
            throw new ImageLoadException();
        }
    }
    public void deleteImage(String imageName, ImageType imageType){
        Path imagePath;
        switch (imageType){
            case AVATAR -> imagePath = Path.of(imageAvatarDirectory, imageName);
            case ICON -> imagePath = Path.of(imageIconDirectory, imageName);
            default -> imagePath = Path.of(imageDefaultDirectory, imageName);
        }
        File fileToDelete = FileUtils.getFile(imagePath.toFile());
        FileUtils.deleteQuietly(fileToDelete);
    }
    private MultipartFile convertToJpeg(MultipartFile originalImage) throws IllegalStateException, IOException {
        final BufferedImage image = ImageIO.read(originalImage.getInputStream());

        final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final boolean canWrite = ImageIO.write(convertedImage, "jpeg", baos);
        baos.flush();

        MultipartFile multipartFile = new MultipartImage(baos.toByteArray(), originalImage.getName(), originalImage.getOriginalFilename(),"image/jpeg", baos.size());

        if (!canWrite) {
            throw new IllegalStateException();
        }
        return multipartFile;
    }
}
