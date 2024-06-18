package org.webCrawler.api;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webCrawler.common.OCRService;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ocr")
public class OCRController {

    @Autowired
    private OCRService ocrService;

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractTextFromPDF(@RequestBody MultipartFile file) {
        try {
            // Save the uploaded file to a temporary location
            File tempFile = File.createTempFile("uploaded_", file.getOriginalFilename());
            file.transferTo(tempFile);
            // Perform OCR on the PDF file
            String extractedText = ocrService.extractTextFromPDF(tempFile);
            // Delete the temporary file
            tempFile.delete();
            return ResponseEntity.ok(extractedText);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(500).body("Error processing PDF file: " + e.getMessage());
        }
    }

    @PostMapping("/extractTableFromImage")
    public ResponseEntity<?> extractTableFromImage(@RequestParam("image") MultipartFile image) {
        try {
            File tempFile = File.createTempFile("uploaded", null);
            image.transferTo(tempFile);
            List<List<String>> table = ocrService.extractTableFromImage(tempFile);
            return ResponseEntity.ok(table);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/extractTablesFromPdf")
    public ResponseEntity<?> extractTablesFromPdf(@RequestParam("pdf") MultipartFile pdf) {
        try {
            File tempFile = File.createTempFile("uploaded", null);
            pdf.transferTo(tempFile);
            List<List<List<String>>> tables = ocrService.extractTablesFromPdf(tempFile);
            return ResponseEntity.ok(tables);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/extractTextFromImage")
    public ResponseEntity<String> extractTextFromImage(@RequestParam("image") MultipartFile image) {
        try {
            File tempFile = File.createTempFile("uploaded", null);
            image.transferTo(tempFile);
            String extractedText = ocrService.extractTextFromImage(tempFile);
            return ResponseEntity.ok(extractedText);
        } catch (IOException | TesseractException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
