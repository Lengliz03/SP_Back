package com.textile.stock.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fournisseurs")
@Data
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String email;

    private String telephone;

    private String adresse;

    private String pays;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "fournisseur")
    private List<Produit> produits = new ArrayList<>();

    @OneToMany(mappedBy = "fournisseur")
    private List<Commande> commandes = new ArrayList<>();
}