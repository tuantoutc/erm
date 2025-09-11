package com.example.erm_demo.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public interface RiskFileService {

    String saveFile(MultipartFile file) throws IOException, NoSuchAlgorithmException;
    String getFileExtension(String filename);
}
