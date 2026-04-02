package com.textile.stock.dto;

import lombok.Data;

@Data
public class FournisseurDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String pays;
    private String notes;
}