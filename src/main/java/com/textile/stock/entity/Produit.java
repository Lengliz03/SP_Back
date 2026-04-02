package com.textile.stock.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "produits")
@Data
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer quantite = 0;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    private Integer seuilAlerte = 10;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
}
