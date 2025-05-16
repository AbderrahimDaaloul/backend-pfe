package com.daaloul.BackEnd.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class OCRService {

    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\Daaloul\\Desktop\\Tess4J\\tessdata"); // Set the correct path
        tesseract.setLanguage("eng"); // Change language if needed
    }

    public String extractText(File imageFile) {
        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new RuntimeException("Error processing file", e);
        }
    }
}

