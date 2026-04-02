package com.textile.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.textile.stock.dto.ProduitDTO;
import com.textile.stock.service.ProduitService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProduitController {
    private final ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAll() {
        return ResponseEntity.ok(produitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProduitDTO> create(@RequestBody ProduitDTO dto) {
        return ResponseEntity.ok(produitService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> update(@PathVariable Long id, @RequestBody ProduitDTO dto) {
        return ResponseEntity.ok(produitService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alertes")
    public ResponseEntity<List<ProduitDTO>> getProduitsStockFaible() {
        return ResponseEntity.ok(produitService.getProduitsStockFaible());
    }
}
