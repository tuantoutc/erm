package com.example.erm_demo.util;

import org.springframework.web.multipart.MultipartFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class FileValidationUtil {
    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "application/pdf",
            "text/plain",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB

    public static boolean isValidFileType(MultipartFile file) {
        return ALLOWED_TYPES.contains(file.getContentType());
    }

    public static boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_SIZE;
    }

    public static String generateFileHash(byte[] fileContent) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(fileContent);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
