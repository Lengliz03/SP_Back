package com.textile.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDTO {
    private Long totalProduits;
    private Long totalFournisseurs;
    private Long totalCommandes;
    private Integer stockTotal;
    private Integer produitsEnAlerte;
    private BigDecimal valeurTotaleStock;
    private Long mouvementsEntree;
    private Long mouvementsSortie;
    private Long commandesEnAttente;
    private Long commandesRecues;
}