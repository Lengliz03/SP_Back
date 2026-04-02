package com.textile.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProduitStatsDTO {
    private String nom;
    private String reference;
    private Integer quantite;
    private BigDecimal valeur;
}