package com.demo.eberber.controller;

import com.demo.eberber.domain.Barber;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.AmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class BucketController {

    private AmazonService amazonService;

    @Autowired
    BucketController(AmazonService amazonService) {
        this.amazonService = amazonService;
    }

    @PostMapping("/uploadFile/{barberId}")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file,  @PathVariable Long barberId) throws ResourceNotFoundException {

        return this.amazonService.uploadFile(file, barberId );
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonService.deleteFileFromS3Bucket(fileUrl);
    }
}
