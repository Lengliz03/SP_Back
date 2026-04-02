package com.textile.stock.dto;

import com.textile.stock.entity.TypeMouvement;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MouvementDTO {
    private Long id;
    private TypeMouvement type;
    private Integer quantite;
    private LocalDateTime date;
    private String motif;
    private String reference;

    // Informations produit
    private Long produitId;
    private String produitNom;
    private String produitReference;

    // Informations utilisateur
    private Long userId;
    private String username;
}