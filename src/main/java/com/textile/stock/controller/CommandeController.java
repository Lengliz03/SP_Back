package com.textile.stock.controller;

import com.textile.stock.dto.CommandeDTO;
import com.textile.stock.dto.CreateCommandeRequest;
import com.textile.stock.entity.StatutCommande;
import com.textile.stock.entity.User;
import com.textile.stock.service.CommandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeDTO> create(
            @Valid @RequestBody CreateCommandeRequest request,
            Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(commandeService.create(request, authenticatedUser));
    }

    @GetMapping
    public ResponseEntity<List<CommandeDTO>> getAll() {
        return ResponseEntity.ok(commandeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.findById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CommandeDTO>> getByStatut(@PathVariable StatutCommande statut) {
        return ResponseEntity.ok(commandeService.findByStatut(statut));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public ResponseEntity<List<CommandeDTO>> getByFournisseur(@PathVariable Long fournisseurId) {
        return ResponseEntity.ok(commandeService.findByFournisseur(fournisseurId));
    }

    @PostMapping("/{id}/recevoir")
    public ResponseEntity<CommandeDTO> recevoir(
            @PathVariable Long id,
            Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(commandeService.recevoirCommande(id, authenticatedUser));
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<CommandeDTO> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.annulerCommande(id));
    }
}