package com.demo.eberber.controller;

import com.demo.eberber.Dto.GeneralDto;
import com.demo.eberber.exception.ResourceNotFoundException;
import com.demo.eberber.service.AmazonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class BucketController {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AmazonService amazonService;

    @Autowired
    BucketController(AmazonService amazonService) {
        this.amazonService = amazonService;
    }

    @PostMapping("/uploadFile/{barberId}")
    public ResponseEntity<GeneralDto.Response> uploadFile(@RequestPart(value = "file") MultipartFile file, @PathVariable Long barberId) throws ResourceNotFoundException {
        try{
            GeneralDto.Response result = new GeneralDto.Response();
            if(file == null){
                result.Error = true;
                result.Message = "Boş dosya gönderdiniz.";
                return ResponseEntity.ok(result);
            }
            result.data = this.amazonService.uploadFile(file, barberId );
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<GeneralDto.Response> deleteFile(@RequestPart(value = "url") String fileUrl) {
        try {
            GeneralDto.Response result = new GeneralDto.Response();
            if(fileUrl == null){
                result.Error = true;
                result.Message = "Boş url gönderdiniz.";
                return ResponseEntity.ok(result);
            }
            result.data = this.amazonService.deleteFileFromS3Bucket(fileUrl);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
