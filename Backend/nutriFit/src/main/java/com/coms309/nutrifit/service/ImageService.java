package com.coms309.nutrifit.service;

import com.coms309.nutrifit.entity.ImageData;
import com.coms309.nutrifit.repo.ImageRepository;
import com.coms309.nutrifit.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public String saveImage(MultipartFile file) throws IOException {



            ImageData imageData = imageRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename()).type(file.getContentType())
                    .pictureData(ImageUtils.compressImage(file.getBytes())).build() );




       if(imageData != null) {
           return  file.getOriginalFilename();
       }
       return null;
    }


    public byte[] downloadImage(String fileName) {
       ImageData dbImg = imageRepository.findByName(fileName);
       byte[] imageData = ImageUtils.decompressImage(dbImg.getPictureData());
       return imageData;
    }
}
