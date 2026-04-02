package com.textile.stock.service;

import com.textile.stock.dto.CreateMouvementRequest;
import com.textile.stock.dto.MouvementDTO;
import com.textile.stock.entity.*;
import com.textile.stock.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MouvementService {

    private final MouvementRepository mouvementRepository;
    private final ProduitRepository produitRepository;

    @Transactional
    public MouvementDTO createMouvement(CreateMouvementRequest request, User authenticatedUser) {
        log.info("Création d'un mouvement: type={}, quantite={}, produitId={}, username={}",
                request.getType(), request.getQuantite(), request.getProduitId(), authenticatedUser.getUsername());

        // Récupérer le produit
        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> {
                    log.error("Produit non trouvé: id={}", request.getProduitId());
                    return new RuntimeException("Produit non trouvé");
                });

        // Utiliser l'utilisateur authentifié directement
        User user = authenticatedUser;

        // Vérifier la quantité pour les sorties
        if (request.getType() == TypeMouvement.SORTIE) {
            if (produit.getQuantite() < request.getQuantite()) {
                log.error("Stock insuffisant: actuel={}, demandé={}", produit.getQuantite(), request.getQuantite());
                throw new RuntimeException(
                        "Stock insuffisant. Stock actuel: " + produit.getQuantite() +
                                ", Quantité demandée: " + request.getQuantite());
            }
        }

        // Créer le mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setType(request.getType());
        mouvement.setQuantite(request.getQuantite());
        mouvement.setMotif(request.getMotif());
        mouvement.setReference(request.getReference());
        mouvement.setProduit(produit);
        mouvement.setUser(user);
        mouvement.setDate(LocalDateTime.now());

        // Mettre à jour la quantité du produit
        if (request.getType() == TypeMouvement.ENTREE) {
            produit.setQuantite(produit.getQuantite() + request.getQuantite());
            log.info("Entrée: nouvelle quantité du produit {}: {}", produit.getId(), produit.getQuantite());
        } else {
            produit.setQuantite(produit.getQuantite() - request.getQuantite());
            log.info("Sortie: nouvelle quantité du produit {}: {}", produit.getId(), produit.getQuantite());
        }

        try {
            produitRepository.save(produit);
            mouvementRepository.save(mouvement);
            log.info("Mouvement créé avec succès: id={}", mouvement.getId());
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la sauvegarde du mouvement");
        }

        return convertToDTO(mouvement);
    }

    public List<MouvementDTO> getAllMouvements() {
        return mouvementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MouvementDTO> getRecentMouvements() {
        return mouvementRepository.findTop10ByOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MouvementDTO> getMouvementsByProduit(Long produitId) {
        return mouvementRepository.findByProduitIdOrderByDateDesc(produitId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MouvementDTO> getMouvementsByType(TypeMouvement type) {
        return mouvementRepository.findByTypeOrderByDateDesc(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MouvementDTO> getMouvementsByPeriod(LocalDateTime debut, LocalDateTime fin) {
        return mouvementRepository.findByDateBetweenOrderByDateDesc(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MouvementDTO convertToDTO(Mouvement mouvement) {
        MouvementDTO dto = new MouvementDTO();
        dto.setId(mouvement.getId());
        dto.setType(mouvement.getType());
        dto.setQuantite(mouvement.getQuantite());
        dto.setDate(mouvement.getDate());
        dto.setMotif(mouvement.getMotif());
        dto.setReference(mouvement.getReference());

        if (mouvement.getProduit() != null) {
            dto.setProduitId(mouvement.getProduit().getId());
            dto.setProduitNom(mouvement.getProduit().getNom());
            dto.setProduitReference(mouvement.getProduit().getReference());
        }

        if (mouvement.getUser() != null) {
            dto.setUserId(mouvement.getUser().getId());
            dto.setUsername(mouvement.getUser().getUsername());
        }

        return dto;
    }
}