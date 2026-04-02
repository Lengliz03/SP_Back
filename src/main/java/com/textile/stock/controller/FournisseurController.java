package com.textile.stock.controller;

import com.textile.stock.dto.FournisseurDTO;
import com.textile.stock.service.FournisseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @GetMapping
    public ResponseEntity<List<FournisseurDTO>> getAll() {
        return ResponseEntity.ok(fournisseurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fournisseurService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FournisseurDTO> create(@RequestBody FournisseurDTO dto) {
        return ResponseEntity.ok(fournisseurService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO> update(@PathVariable Long id, @RequestBody FournisseurDTO dto) {
        return ResponseEntity.ok(fournisseurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}