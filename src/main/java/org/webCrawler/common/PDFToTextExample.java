package org.webCrawler.common;


import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class PDFToTextExample {
    /**
     * The original PDF that will be parsed.
     */
    public static final String PREFACE = "C:/Users/p.ahmadi/Downloads/vsina.pdf";
    /**
     * The resulting text file.
     */
    public static final String RESULT = "C:/Users/p.ahmadi/Downloads/preface.txt";

    /**
     * Parses a PDF to a plain text file.
     *
     * @param pdf the original PDF
     * @param txt the resulting text
     * @throws IOException
     */
    public void parsePdf(String pdf, String txt) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            String word = strategy.getResultantText();
            if (CommonUtils.isUTF8(word)) {
                String reverse = CommonUtils.reverse(word);
                out.println(reverse);
            } else {
                out.println(word);
            }
            reader.close();
            out.flush();
            out.close();
        }
    }
}
