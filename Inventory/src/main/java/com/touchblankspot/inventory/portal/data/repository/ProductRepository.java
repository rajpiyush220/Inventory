package com.touchblankspot.inventory.portal.data.repository;

import com.touchblankspot.inventory.portal.data.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {}
