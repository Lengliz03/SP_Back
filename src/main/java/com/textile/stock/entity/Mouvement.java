package com.textile.stock.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements")
@Data
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement type;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    private String motif;

    private String reference;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}