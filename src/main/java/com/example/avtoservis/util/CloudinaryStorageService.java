package com.example.avtoservis.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    public StorageService.StorageResult uploadImage(MultipartFile file) throws IOException {
        try {
            log.info("Uploading image: {}", file.getOriginalFilename());

            // Параметры загрузки с поддержкой HEIC
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "vadim-harovyuk-dev",
                    "resource_type", "image",
                    "quality", "auto",
                    "fetch_format", "auto",
                    "format", "jpg"  // Принудительная конвертация в JPEG
            );

            // Если это HEIC файл, добавляем специальные параметры
            if (isHeicFormat(file.getOriginalFilename(), file.getContentType())) {
                log.info("Detected HEIC format, converting to JPEG");
                uploadParams.put("format", "jpg");
                uploadParams.put("quality", "auto:best");
            }

            // Загрузка файла
            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), uploadParams);

            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            log.info("✅ Image uploaded successfully: {} with URL: {}", publicId, imageUrl);

            return new StorageResult(imageUrl, publicId);

        } catch (IOException e) {
            log.error("❌ Error uploading image to Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ Unexpected error during image upload: {}", e.getMessage(), e);
            throw new IOException("Unexpected error during image upload: " + e.getMessage(), e);
        }
    }

    private boolean isHeicFormat(String filename, String contentType) {
        if (filename != null) {
            String lowerFilename = filename.toLowerCase();
            if (lowerFilename.endsWith(".heic") || lowerFilename.endsWith(".heif")) {
                return true;
            }
        }

        if (contentType != null) {
            String lowerContentType = contentType.toLowerCase();
            if (lowerContentType.contains("heic") ||
                    lowerContentType.contains("heif") ||
                    lowerContentType.contains("image/heic") ||
                    lowerContentType.contains("image/heif")) {
                return true;
            }
        }

        return false;
    }


    @Override
    public boolean deleteImage(String imageId) {
        try {
            log.info("Deleting image: {}", imageId);

            Map<String, Object> result = cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");

            boolean deleted = "ok".equals(resultStatus);

            if (deleted) {
                log.info("✅ Image deleted successfully: {}", imageId);
            } else {
                log.warn("⚠️ Failed to delete image: {}, result: {}", imageId, resultStatus);
            }

            return deleted;

        } catch (IOException e) {
            log.error("❌ Error deleting image from Cloudinary: {}", imageId, e);
            return false;
        }
    }
}

