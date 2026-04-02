package com.textile.stock.dto;

import com.textile.stock.entity.TypeMouvement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateMouvementRequest {
    @NotNull(message = "Le type de mouvement est requis")
    private TypeMouvement type;

    @NotNull(message = "La quantité est requise")
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;

    private String motif;

    private String reference;

    @NotNull(message = "L'ID du produit est requis")
    private Long produitId;
}