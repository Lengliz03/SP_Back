package com.textile.stock.service;

import com.textile.stock.dto.*;
import com.textile.stock.entity.*;
import com.textile.stock.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;
    private final CommandeRepository commandeRepository;
    private final MouvementRepository mouvementRepository;

    public StatsDTO getGlobalStats() {
        List<Produit> produits = produitRepository.findAll();

        StatsDTO stats = new StatsDTO();

        // Statistiques de base
        stats.setTotalProduits((long) produits.size());
        stats.setTotalFournisseurs(fournisseurRepository.count());
        stats.setTotalCommandes(commandeRepository.count());

        // Stock
        stats.setStockTotal(produits.stream()
                .mapToInt(Produit::getQuantite)
                .sum());

        stats.setProduitsEnAlerte((int) produits.stream()
                .filter(p -> p.getQuantite() <= p.getSeuilAlerte())
                .count());

        // Valeur totale du stock
        stats.setValeurTotaleStock(produits.stream()
                .map(p -> p.getPrixUnitaire().multiply(new BigDecimal(p.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Mouvements
        stats.setMouvementsEntree(mouvementRepository.findByTypeOrderByDateDesc(TypeMouvement.ENTREE).stream().count());
        stats.setMouvementsSortie(mouvementRepository.findByTypeOrderByDateDesc(TypeMouvement.SORTIE).stream().count());

        // Commandes
        stats.setCommandesEnAttente(
                commandeRepository.findByStatutOrderByDateCommandeDesc(StatutCommande.EN_ATTENTE).stream().count());
        stats.setCommandesRecues(
                commandeRepository.findByStatutOrderByDateCommandeDesc(StatutCommande.RECUE).stream().count());

        return stats;
    }

    public List<ProduitStatsDTO> getTopProduits(int limit) {
        return produitRepository.findAll().stream()
                .sorted((p1, p2) -> {
                    BigDecimal val1 = p1.getPrixUnitaire().multiply(new BigDecimal(p1.getQuantite()));
                    BigDecimal val2 = p2.getPrixUnitaire().multiply(new BigDecimal(p2.getQuantite()));
                    return val2.compareTo(val1);
                })
                .limit(limit)
                .map(p -> new ProduitStatsDTO(
                        p.getNom(),
                        p.getReference(),
                        p.getQuantite(),
                        p.getPrixUnitaire().multiply(new BigDecimal(p.getQuantite()))))
                .collect(Collectors.toList());
    }

    public List<MouvementStatsDTO> getMouvementsParJour(int jours) {
        LocalDateTime dateDebut = LocalDateTime.now().minusDays(jours);
        List<Mouvement> mouvements = mouvementRepository.findByDateBetweenOrderByDateDesc(
                dateDebut, LocalDateTime.now());

        Map<String, Map<TypeMouvement, Long>> mouvementsParJour = mouvements.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM")),
                        Collectors.groupingBy(Mouvement::getType, Collectors.counting())));

        return mouvementsParJour.entrySet().stream()
                .map(entry -> new MouvementStatsDTO(
                        entry.getKey(),
                        entry.getValue().getOrDefault(TypeMouvement.ENTREE, 0L),
                        entry.getValue().getOrDefault(TypeMouvement.SORTIE, 0L)))
                .sorted(Comparator.comparing(MouvementStatsDTO::getDate))
                .collect(Collectors.toList());
    }
}