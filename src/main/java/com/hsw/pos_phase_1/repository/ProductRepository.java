package com.hsw.pos_phase_1.repository;

import com.hsw.pos_phase_1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query example
    Product findByName(String name);
}
