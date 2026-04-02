package com.textile.stock.service;

import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.textile.stock.dto.ProduitDTO;
import com.textile.stock.entity.Produit;
import com.textile.stock.repository.*;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final FournisseurRepository fournisseurRepository;

    public List<ProduitDTO> findAll() {
        return produitRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProduitDTO findById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return convertToDTO(produit);
    }

    public ProduitDTO create(ProduitDTO dto) {
        if (produitRepository.existsByReference(dto.getReference())) {
            throw new RuntimeException("La référence existe déjà");
        }
        Produit produit = convertToEntity(dto);
        return convertToDTO(produitRepository.save(produit));
    }

    public ProduitDTO update(Long id, ProduitDTO dto) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setQuantite(dto.getQuantite());
        produit.setPrixUnitaire(dto.getPrixUnitaire());
        produit.setSeuilAlerte(dto.getSeuilAlerte());

        if (dto.getCategorieId() != null) {
            produit.setCategorie(categorieRepository.findById(dto.getCategorieId()).orElse(null));
        }

        if (dto.getFournisseurId() != null) {
            produit.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId()).orElse(null));
        }

        return convertToDTO(produitRepository.save(produit));
    }

    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    private ProduitDTO convertToDTO(Produit produit) {
        ProduitDTO dto = new ProduitDTO();
        dto.setId(produit.getId());
        dto.setReference(produit.getReference());
        dto.setNom(produit.getNom());
        dto.setDescription(produit.getDescription());
        dto.setQuantite(produit.getQuantite());
        dto.setPrixUnitaire(produit.getPrixUnitaire());
        dto.setSeuilAlerte(produit.getSeuilAlerte());

        if (produit.getCategorie() != null) {
            dto.setCategorieId(produit.getCategorie().getId());
            dto.setCategorieNom(produit.getCategorie().getNom());
        }

        if (produit.getFournisseur() != null) {
            dto.setFournisseurId(produit.getFournisseur().getId());
            dto.setFournisseurNom(produit.getFournisseur().getNom());
        }

        return dto;
    }

    private Produit convertToEntity(ProduitDTO dto) {
        Produit produit = new Produit();
        produit.setReference(dto.getReference());
        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setQuantite(dto.getQuantite());
        produit.setPrixUnitaire(dto.getPrixUnitaire());
        produit.setSeuilAlerte(dto.getSeuilAlerte());

        if (dto.getCategorieId() != null) {
            produit.setCategorie(categorieRepository.findById(dto.getCategorieId()).orElse(null));
        }

        if (dto.getFournisseurId() != null) {
            produit.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId()).orElse(null));
        }

        return produit;
    }

    public List<ProduitDTO> getProduitsStockFaible() {
        return produitRepository.findAll().stream()
                .filter(p -> p.getQuantite() <= p.getSeuilAlerte())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
