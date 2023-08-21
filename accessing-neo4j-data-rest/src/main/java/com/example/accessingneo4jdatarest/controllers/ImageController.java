package com.example.accessingneo4jdatarest.controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;

@RestController
@RequestMapping("api/images")
public class ImageController {
	@Autowired
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @PostMapping("/upload/{bucketName}")
    public ResponseEntity<String> uploadImage(@PathVariable String bucketName, @RequestParam("file") MultipartFile file) {
        try {
            // Check if the bucket exists
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                // Create the bucket if it doesn't exist
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String contentType = file.getContentType();

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, fileSize, -1)
                    .contentType(contentType)
                    .build()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, List<String>>> listAllBucketsAndImages() {
        try {
            Map<String, List<String>> bucketImagesMap = new HashMap<>();

            Iterable<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                String bucketName = bucket.name();

                List<String> objectNames = new ArrayList<>();
                Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
                for (Result<Item> result : results) {
                    objectNames.add(result.get().objectName());
                }

                bucketImagesMap.put(bucketName, objectNames);
            }

            return ResponseEntity.ok().body(bucketImagesMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<List<String>> listObjects(@PathVariable String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                // Return a 404 response if the bucket doesn't exist
                String errorMessage = "Bucket '" + bucketName + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonList(errorMessage));
            }

            List<String> objectNames = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());

            for (Result<Item> result : results) {
                objectNames.add(result.get().objectName());
            }

            return ResponseEntity.ok().body(objectNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // returns URL
    @GetMapping("/{bucketName}/{imageName}")
    public ResponseEntity<String> getImage(@PathVariable String imageName, @PathVariable String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                // Return a 404 response if the bucket doesn't exist
                // String errorMessage = "Bucket '" + bucketName + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(imageName)
                    .build()
            );
            
            byte[] imageBytes = response.readAllBytes();

            if (imageBytes == null) {
                // return 404 if image not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String imageUrl =  minioEndpoint + "/" + bucketName + "/" + imageName;
            return ResponseEntity.ok().body(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{bucketName}")
    public ResponseEntity<String> deleteBucket(@PathVariable String bucketName){
        try {
            // check if bucket exists
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                String errorMessage = "Bucket '" + bucketName + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return ResponseEntity.ok().body("Bucket deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete bucket.");
        }
    }

    @DeleteMapping("/delete/{bucketName}/{objectName}")
    public ResponseEntity<String> deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            // check if bucket exists
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                String errorMessage = "Bucket '" + bucketName + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            // Check if the object exists
            boolean objectExists;
            try {
                minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
                objectExists = true;
            } catch (ErrorResponseException e) {
                objectExists = false;
            }

            if (!objectExists) {
                // Handle object not found scenario
                String errorMessage = "Object '" + objectName + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return ResponseEntity.ok().body("Object deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete object.");
        }
    }
}
