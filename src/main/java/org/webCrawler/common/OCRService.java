package org.webCrawler.common;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@Service
public class OCRService {

    private final ITesseract tesseract;

    public OCRService() {
        this.tesseract = new Tesseract();
        // Set the path to your Tesseract installation
        tesseract.setDatapath("D:/Tesseract-OCR/tessdata");
        tesseract.setLanguage("fas"); // Set language
//        tesseract.setLanguage("eng");
    }

    public String extractTextFromPDF(File pdfFile) throws IOException, TesseractException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            StringBuilder extractedText = new StringBuilder();

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
                File tempImageFile = File.createTempFile("temp_page_" + page, ".png");
                ImageIO.write(image, "png", tempImageFile);

                String text = tesseract.doOCR(tempImageFile);
                extractedText.append(text).append("\n");

                tempImageFile.delete();
            }
            return extractedText.toString();
        }
    }
}