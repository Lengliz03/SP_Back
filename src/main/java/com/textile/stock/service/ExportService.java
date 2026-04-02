package com.textile.stock.service;

import com.textile.stock.entity.*;
import com.textile.stock.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final ProduitRepository produitRepository;
    private final MouvementRepository mouvementRepository;

    public byte[] exportProduitsToExcel() throws IOException {
        List<Produit> produits = produitRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Produits");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Référence", "Nom", "Description", "Quantité", "Prix Unitaire", "Seuil Alerte",
                    "Catégorie", "Fournisseur" };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;
            for (Produit produit : produits) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(produit.getReference());
                row.createCell(1).setCellValue(produit.getNom());
                row.createCell(2).setCellValue(produit.getDescription());
                row.createCell(3).setCellValue(produit.getQuantite());
                row.createCell(4).setCellValue(produit.getPrixUnitaire().doubleValue());
                row.createCell(5).setCellValue(produit.getSeuilAlerte());
                row.createCell(6).setCellValue(produit.getCategorie() != null ? produit.getCategorie().getNom() : "");
                row.createCell(7)
                        .setCellValue(produit.getFournisseur() != null ? produit.getFournisseur().getNom() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportMouvementsToExcel() throws IOException {
        List<Mouvement> mouvements = mouvementRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mouvements");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Date", "Type", "Produit", "Quantité", "Motif", "Référence", "Utilisateur" };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            int rowNum = 1;
            for (Mouvement mouvement : mouvements) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(mouvement.getDate().format(formatter));
                row.createCell(1).setCellValue(mouvement.getType().toString());
                row.createCell(2).setCellValue(mouvement.getProduit() != null ? mouvement.getProduit().getNom() : "");
                row.createCell(3).setCellValue(mouvement.getQuantite());
                row.createCell(4).setCellValue(mouvement.getMotif());
                row.createCell(5).setCellValue(mouvement.getReference());
                row.createCell(6).setCellValue(mouvement.getUser() != null ? mouvement.getUser().getUsername() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}