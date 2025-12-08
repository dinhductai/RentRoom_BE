package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.exception.MyFileNotFoundException;
import com.cntt.rentalmanagement.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;



    @GetMapping("/view-file/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Decode URL-encoded filename
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        log.info("📥 Downloading file: {}", decodedFileName);
        
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(decodedFileName);

        // Determine content type based on file extension
        String contentType = getContentType(decodedFileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // Endpoint for direct file access with filename as path (for URLs like /filename.pdf)
    @GetMapping(value = "/{fileName}", 
                produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<Resource> viewFileByName(@PathVariable String fileName, HttpServletRequest request) {
        // Only match if filename has a file extension
        if (!fileName.contains(".")) {
            throw new MyFileNotFoundException("Not a file URL");
        }
        
        // Decode URL-encoded filename (e.g., %20 -> space)
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        log.info("👁️ Viewing file: {} (original: {})", decodedFileName, fileName);
        
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(decodedFileName);

        // Determine content type based on file extension
        String contentType = getContentType(decodedFileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }
    
    private String getContentType(String fileName) {
        String lowerCase = fileName.toLowerCase();
        if (lowerCase.endsWith(".pdf")) return "application/pdf";
        if (lowerCase.endsWith(".doc")) return "application/msword";
        if (lowerCase.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lowerCase.endsWith(".xls")) return "application/vnd.ms-excel";
        if (lowerCase.endsWith(".xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) return "image/jpeg";
        if (lowerCase.endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(filename);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/document/{filename:.+}")
    public ResponseEntity<byte[]> getDocument(@PathVariable String filename) throws IOException, TikaException {
        Resource resource = fileStorageService.loadFileAsResource(filename);

        Tika tika = new Tika();
        String mimeType = tika.detect(resource.getFile());

        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(mimeType));
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(resource.getFilename()).build());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
