package com.pedro.productjwtapi.repository;

import com.pedro.productjwtapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
