package com.textile.stock.controller;

import com.textile.stock.dto.CreateMouvementRequest;
import com.textile.stock.dto.MouvementDTO;
import com.textile.stock.entity.TypeMouvement;
import com.textile.stock.entity.User;
import com.textile.stock.service.MouvementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mouvements")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MouvementController {

    private final MouvementService mouvementService;

    @PostMapping
    public ResponseEntity<MouvementDTO> createMouvement(
            @Valid @RequestBody CreateMouvementRequest request,
            Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(mouvementService.createMouvement(request, authenticatedUser));
    }

    @GetMapping
    public ResponseEntity<List<MouvementDTO>> getAllMouvements() {
        return ResponseEntity.ok(mouvementService.getAllMouvements());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MouvementDTO>> getRecentMouvements() {
        return ResponseEntity.ok(mouvementService.getRecentMouvements());
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<MouvementDTO>> getMouvementsByProduit(@PathVariable Long produitId) {
        return ResponseEntity.ok(mouvementService.getMouvementsByProduit(produitId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MouvementDTO>> getMouvementsByType(@PathVariable TypeMouvement type) {
        return ResponseEntity.ok(mouvementService.getMouvementsByType(type));
    }

    @GetMapping("/period")
    public ResponseEntity<List<MouvementDTO>> getMouvementsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(mouvementService.getMouvementsByPeriod(debut, fin));
    }
}