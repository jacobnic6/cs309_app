package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.repo.ImageRepository;
import com.coms309.nutrifit.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The type Image service.
 */
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    /**
     * Save image image data.
     *
     * @param file the file
     * @return the image data
     * @throws IOException the io exception
     */
    public ImageData saveImage(MultipartFile file) throws IOException {
        ImageData existing = imageRepository.findByName(file.getOriginalFilename());
    if(existing != null){
        return existing;
    }

        ImageData imageData = imageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename()).type(file.getContentType())
                .pictureData(ImageUtils.compressImage(file.getBytes())).build());


        return imageData;
    }
    public boolean imageExists(String fileName){
        return imageRepository.existsByNameAndTypeAllIgnoreCase(fileName);
    }


    /**
     * Download image byte [ ].
     *
     * @param fileName the file name
     * @return the byte [ ]
     */
    public byte[] downloadImage(String fileName) {
        ImageData dbImg = imageRepository.findByName(fileName);
        byte[] imageData = ImageUtils.decompressImage(dbImg.getPictureData());
        return imageData;
    }

    public byte[] downloadDefaultImage() {
        ImageData dbImg = imageRepository.findByName("default-pic.png");
        byte[] imageData = ImageUtils.decompressImage(dbImg.getPictureData());
        return imageData;
    }
}
