package com.textile.stock.dto;

import com.textile.stock.entity.StatutCommande;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommandeDTO {
    private Long id;
    private String numero;
    private LocalDateTime dateCommande;
    private LocalDateTime dateReception;
    private StatutCommande statut;
    private BigDecimal montantTotal;
    private String notes;
    private Long fournisseurId;
    private String fournisseurNom;
    private String username;
    private List<LigneCommandeDTO> lignes = new ArrayList<>();
}