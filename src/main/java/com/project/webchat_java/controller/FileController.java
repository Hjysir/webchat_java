package com.project.webchat_java.controller;

import com.project.webchat_java.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/api/upload/{username}/{chatid}")
    public ResponseEntity<?> uploadFile(@RequestParam("username") String username, @PathVariable String chatname, @RequestParam("file") MultipartFile uploadfile) {

        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>("ERROR", HttpStatus.OK);
        }

        try {
            fileService.saveUploadedFiles(username, chatname, uploadfile);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Success - " + uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }
}