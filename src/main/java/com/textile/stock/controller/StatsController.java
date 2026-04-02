package com.textile.stock.controller;

import com.textile.stock.dto.MouvementStatsDTO;
import com.textile.stock.dto.ProduitStatsDTO;
import com.textile.stock.dto.StatsDTO;
import com.textile.stock.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/global")
    public ResponseEntity<StatsDTO> getGlobalStats() {
        return ResponseEntity.ok(statsService.getGlobalStats());
    }

    @GetMapping("/top-produits")
    public ResponseEntity<List<ProduitStatsDTO>> getTopProduits(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(statsService.getTopProduits(limit));
    }

    @GetMapping("/mouvements-par-jour")
    public ResponseEntity<List<MouvementStatsDTO>> getMouvementsParJour(
            @RequestParam(defaultValue = "7") int jours) {
        return ResponseEntity.ok(statsService.getMouvementsParJour(jours));
    }
}
