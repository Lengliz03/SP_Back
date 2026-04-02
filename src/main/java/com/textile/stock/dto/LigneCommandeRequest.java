package com.textile.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LigneCommandeRequest {
    @NotNull(message = "Le produit est requis")
    private Long produitId;

    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est requis")
    @Positive(message = "Le prix unitaire doit être positif")
    private BigDecimal prixUnitaire;
}