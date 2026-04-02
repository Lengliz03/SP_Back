package com.textile.stock.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateCommandeRequest {
    @NotNull(message = "Le fournisseur est requis")
    private Long fournisseurId;

    private String notes;

    @NotEmpty(message = "La commande doit contenir au moins un produit")
    private List<LigneCommandeRequest> lignes;
}