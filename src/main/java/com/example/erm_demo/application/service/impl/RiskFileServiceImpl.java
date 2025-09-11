package com.example.erm_demo.application.service.impl;


import com.example.erm_demo.application.service.RiskFileService;
import com.example.erm_demo.util.FileValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class RiskFileServiceImpl implements RiskFileService {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Override
    public String saveFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {

        // Validate file
        if (!FileValidationUtil.isValidFileType(file)) {
            throw new IllegalArgumentException("Loại file không được hỗ trợ. Chỉ chấp nhận PDF, TXT, DOC");
        }

        if (!FileValidationUtil.isValidFileSize(file)) {
            throw new IllegalArgumentException("Kích thước file vượt quá giới hạn cho phép");
        }

        // Generate hash to check duplicate
        byte[] fileBytes = file.getBytes();
        String fileHash = FileValidationUtil.generateFileHash(fileBytes);

        // Generate random filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String randomFileName = UUID.randomUUID().toString() + "_" + fileHash.substring(0, 8) + "." + fileExtension;

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        Path filePath = uploadPath.resolve(randomFileName);
        Files.write(filePath, fileBytes);

        return filePath.toString();
    }

    @Override
    public String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    public boolean checkFileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

}
