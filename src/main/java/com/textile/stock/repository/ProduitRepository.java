package com.textile.stock.repository;

import com.textile.stock.entity.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findByReference(String reference);

    Boolean existsByReference(String reference);

}
