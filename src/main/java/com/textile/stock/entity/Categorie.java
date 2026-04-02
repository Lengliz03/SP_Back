package com.textile.stock.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name="categories")
@Data
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nom;
    
    private String description;
}
