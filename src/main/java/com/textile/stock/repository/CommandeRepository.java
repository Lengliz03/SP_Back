package com.textile.stock.repository;

import com.textile.stock.entity.Commande;
import com.textile.stock.entity.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Optional<Commande> findByNumero(String numero);

    List<Commande> findByStatutOrderByDateCommandeDesc(StatutCommande statut);

    List<Commande> findByFournisseurIdOrderByDateCommandeDesc(Long fournisseurId);

    List<Commande> findAllByOrderByDateCommandeDesc();
}