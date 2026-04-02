package com.textile.stock.controller;

import com.textile.stock.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/produits/excel")
    public ResponseEntity<byte[]> exportProduitsToExcel() {
        try {
            byte[] excelData = exportService.exportProduitsToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "produits.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/mouvements/excel")
    public ResponseEntity<byte[]> exportMouvementsToExcel() {
        try {
            byte[] excelData = exportService.exportMouvementsToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "mouvements.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}