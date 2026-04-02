package com.textile.stock.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LigneCommandeDTO {
    private Long id;
    private Long produitId;
    private String produitNom;
    private String produitReference;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
}