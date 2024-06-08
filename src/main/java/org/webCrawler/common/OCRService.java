package org.webCrawler.common;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import technology.tabula.ObjectExtractor;
import technology.tabula.PageIterator;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import technology.tabula.Page;
import technology.tabula.Table;

import javax.imageio.ImageIO;

@Service
public class OCRService {

    private final ITesseract tesseract;

    public OCRService() {
        this.tesseract = new Tesseract();
        // Set the path to your Tesseract installation
        tesseract.setDatapath("D:/Tesseract-OCR/tessdata");
        tesseract.setLanguage("fas");
//        tesseract.setLanguage("eng");// Set language
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


    public String extractTextFromImage(File imageFile) throws TesseractException {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                throw new IOException("Could not read image file: " + imageFile.getPath());
            }
            return tesseract.doOCR(image);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TesseractException("Error reading image file", e);
        }
    }

//    public String extractTextFromPDF(File pdfFile) throws IOException, TesseractException {
//        try (PDDocument document = PDDocument.load(pdfFile)) {
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            StringBuilder extractedText = new StringBuilder();
//
//            for (int page = 0; page < document.getNumberOfPages(); ++page) {
//                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
//                File tempImageFile = File.createTempFile("temp_page_" + page, ".png");
//                ImageIO.write(image, "png", tempImageFile);
//
//                try {
//                    String text = tesseract.doOCR(tempImageFile);
//                    extractedText.append(text).append("\n");
//                } finally {
//                    if (!tempImageFile.delete()) {
//                        System.err.println("Failed to delete temporary file: " + tempImageFile.getPath());
//                    }
//                }
//            }
//            return extractedText.toString();
//        }
//    }

    public List<List<String>> extractTableFromImage(File imageFile) throws TesseractException, IOException {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Could not read image file: " + imageFile.getPath());
        }
        String rawText = tesseract.doOCR(image);
        return parseTable(rawText);
    }

    private List<List<String>> parseTable(String rawText) {
        List<List<String>> table = new ArrayList<>();
        String[] rows = rawText.split("\n");

        for (String row : rows) {
            List<String> columns = Arrays.asList(row.split("\\s+")); // Split columns by whitespace
            table.add(columns);
        }

        return table;
    }

    public List<List<List<String>>> extractTablesFromPdf(File pdfFile) throws IOException {
        List<List<List<String>>> allTables = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();

            PageIterator pages = new ObjectExtractor(document).extract();
            for (PageIterator it = pages; it.hasNext(); ) {
                Page page = it.next();
                List<Table> tables = sea.extract(page);
                if (tables.isEmpty()) {
                    tables = bea.extract(page);

                }

                for (Table table : tables) {
                    List<List<String>> parsedTable = new ArrayList<>();
                    for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                        List<String> row = new ArrayList<>();
                        for (int colIndex = 0; colIndex < table.getColCount(); colIndex++) {
                            row.add(table.getCell(rowIndex, colIndex).getText());
                        }
                        parsedTable.add(row);
                    }
                    allTables.add(parsedTable);
                }
            }
        }

        return allTables;
    }



}
