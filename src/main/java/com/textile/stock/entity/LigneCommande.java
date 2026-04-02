package com.textile.stock.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "lignes_commande")
@Data
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private BigDecimal sousTotal;

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @PrePersist
    @PreUpdate
    public void calculerSousTotal() {
        this.sousTotal = this.prixUnitaire.multiply(new BigDecimal(this.quantite));
    }
}