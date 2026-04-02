package com.textile.stock.repository;

import com.textile.stock.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface MouvementRepository extends JpaRepository<com.textile.stock.entity.Mouvement, Long> {

    // Récupérer tous les mouvements d'un produit
    List<com.textile.stock.entity.Mouvement> findByProduitIdOrderByDateDesc(Long produitId);

    // Récupérer les mouvements par type
    List<com.textile.stock.entity.Mouvement> findByTypeOrderByDateDesc(TypeMouvement type);

    // Récupérer les mouvements entre deux dates
    List<com.textile.stock.entity.Mouvement> findByDateBetweenOrderByDateDesc(LocalDateTime debut, LocalDateTime fin);

    // Récupérer les derniers mouvements
    List<com.textile.stock.entity.Mouvement> findTop10ByOrderByDateDesc();

    // Statistiques - Total quantité entrée pour un produit
    @Query("SELECT SUM(m.quantite) FROM Mouvement m WHERE m.produit.id = :produitId AND m.type = 'ENTREE'")
    Integer getTotalEntrees(Long produitId);

    // Statistiques - Total quantité sortie pour un produit
    @Query("SELECT SUM(m.quantite) FROM Mouvement m WHERE m.produit.id = :produitId AND m.type = 'SORTIE'")
    Integer getTotalSorties(Long produitId);
}