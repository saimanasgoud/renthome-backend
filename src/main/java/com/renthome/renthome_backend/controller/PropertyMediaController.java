package com.renthome.renthome_backend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.renthome.renthome_backend.entity.FormData;
import com.renthome.renthome_backend.repository.FormDataRepository;

@RestController
@RequestMapping("/api/property-media")
@CrossOrigin
public class PropertyMediaController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
private FormDataRepository repo;

   @PostMapping(value = "/{propertyId}", consumes = "multipart/form-data")
public ResponseEntity<?> uploadMedia(
    @PathVariable Long propertyId,
    @RequestParam(value = "images", required = false) MultipartFile[] images,
    @RequestParam(value = "video", required = false) MultipartFile video
) throws IOException {
   
   List<String> imageUrls = new ArrayList<>();

if (images != null) {
    for (MultipartFile img : images) {
        if (img != null && !img.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                img.getBytes(),
                ObjectUtils.asMap("folder", "renthome/images")
            );
            imageUrls.add(uploadResult.get("secure_url").toString());
        }
    }
}

String videoUrl = null;
if (video != null && !video.isEmpty()) {
    Map<?, ?> uploadResult = cloudinary.uploader().upload(
        video.getBytes(),
        ObjectUtils.asMap(
            "resource_type", "video",
            "folder", "renthome/videos"
        )
    );
    videoUrl = uploadResult.get("secure_url").toString();
}

FormData property = repo.findById(propertyId).orElseThrow();

property.setImages(imageUrls);

repo.save(property);

return ResponseEntity.ok(Map.of(
    "propertyId", propertyId,
    "images", imageUrls,
    "video", videoUrl
));
    }
}
