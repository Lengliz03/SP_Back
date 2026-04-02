package com.textile.stock.service;

import com.textile.stock.dto.*;
import com.textile.stock.entity.*;
import com.textile.stock.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final MouvementRepository mouvementRepository;

    @Transactional
    public CommandeDTO create(CreateCommandeRequest request, User authenticatedUser) {
        // Récupérer le fournisseur
        Fournisseur fournisseur = fournisseurRepository.findById(request.getFournisseurId())
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        // Utiliser l'utilisateur authentifié directement
        User user = authenticatedUser;

        // Créer la commande
        Commande commande = new Commande();
        commande.setNumero(generateNumeroCommande());
        commande.setFournisseur(fournisseur);
        commande.setUser(user);
        commande.setNotes(request.getNotes());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        // Créer les lignes de commande
        BigDecimal montantTotal = BigDecimal.ZERO;

        for (LigneCommandeRequest ligneReq : request.getLignes()) {
            Produit produit = produitRepository.findById(ligneReq.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé: " + ligneReq.getProduitId()));

            LigneCommande ligne = new LigneCommande();
            ligne.setProduit(produit);
            ligne.setQuantite(ligneReq.getQuantite());
            ligne.setPrixUnitaire(ligneReq.getPrixUnitaire());
            ligne.setCommande(commande);
            ligne.calculerSousTotal();

            commande.getLignes().add(ligne);
            montantTotal = montantTotal.add(ligne.getSousTotal());
        }

        commande.setMontantTotal(montantTotal);

        return convertToDTO(commandeRepository.save(commande));
    }

    @Transactional
    public CommandeDTO recevoirCommande(Long id, User authenticatedUser) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if (commande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new RuntimeException("La commande n'est pas en attente");
        }

        User user = authenticatedUser;

        // Mettre à jour le stock pour chaque ligne
        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();

            // Augmenter le stock
            produit.setQuantite(produit.getQuantite() + ligne.getQuantite());
            produitRepository.save(produit);

            // Créer un mouvement d'entrée
            Mouvement mouvement = new Mouvement();
            mouvement.setType(TypeMouvement.ENTREE);
            mouvement.setQuantite(ligne.getQuantite());
            mouvement.setProduit(produit);
            mouvement.setUser(user);
            mouvement.setMotif("Réception commande " + commande.getNumero());
            mouvement.setReference(commande.getNumero());
            mouvementRepository.save(mouvement);
        }

        // Mettre à jour le statut de la commande
        commande.setStatut(StatutCommande.RECUE);
        commande.setDateReception(LocalDateTime.now());

        return convertToDTO(commandeRepository.save(commande));
    }

    @Transactional
    public CommandeDTO annulerCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if (commande.getStatut() == StatutCommande.RECUE) {
            throw new RuntimeException("Impossible d'annuler une commande déjà reçue");
        }

        commande.setStatut(StatutCommande.ANNULEE);
        return convertToDTO(commandeRepository.save(commande));
    }

    public List<CommandeDTO> findAll() {
        return commandeRepository.findAllByOrderByDateCommandeDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommandeDTO findById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        return convertToDTO(commande);
    }

    public List<CommandeDTO> findByStatut(StatutCommande statut) {
        return commandeRepository.findByStatutOrderByDateCommandeDesc(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CommandeDTO> findByFournisseur(Long fournisseurId) {
        return commandeRepository.findByFournisseurIdOrderByDateCommandeDesc(fournisseurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private String generateNumeroCommande() {
        String prefix = "CMD-";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefix + timestamp;
    }

    private CommandeDTO convertToDTO(Commande commande) {
        CommandeDTO dto = new CommandeDTO();
        dto.setId(commande.getId());
        dto.setNumero(commande.getNumero());
        dto.setDateCommande(commande.getDateCommande());
        dto.setDateReception(commande.getDateReception());
        dto.setStatut(commande.getStatut());
        dto.setMontantTotal(commande.getMontantTotal());
        dto.setNotes(commande.getNotes());

        if (commande.getFournisseur() != null) {
            dto.setFournisseurId(commande.getFournisseur().getId());
            dto.setFournisseurNom(commande.getFournisseur().getNom());
        }

        if (commande.getUser() != null) {
            dto.setUsername(commande.getUser().getUsername());
        }

        // Convertir les lignes
        List<LigneCommandeDTO> lignesDTO = commande.getLignes().stream()
                .map(this::convertLigneToDTO)
                .collect(Collectors.toList());
        dto.setLignes(lignesDTO);

        return dto;
    }

    private LigneCommandeDTO convertLigneToDTO(LigneCommande ligne) {
        LigneCommandeDTO dto = new LigneCommandeDTO();
        dto.setId(ligne.getId());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaire(ligne.getPrixUnitaire());
        dto.setSousTotal(ligne.getSousTotal());

        if (ligne.getProduit() != null) {
            dto.setProduitId(ligne.getProduit().getId());
            dto.setProduitNom(ligne.getProduit().getNom());
            dto.setProduitReference(ligne.getProduit().getReference());
        }

        return dto;
    }
}