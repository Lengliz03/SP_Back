package com.textile.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.textile.stock.entity.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
