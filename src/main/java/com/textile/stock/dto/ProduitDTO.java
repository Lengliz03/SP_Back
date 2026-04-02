package com.textile.stock.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProduitDTO {
    private Long id;
    private String reference;
    private String nom;
    private String description;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private Integer seuilAlerte;
    private Long categorieId;
    private String categorieNom;
    private Long fournisseurId;
    private String fournisseurNom;

}
