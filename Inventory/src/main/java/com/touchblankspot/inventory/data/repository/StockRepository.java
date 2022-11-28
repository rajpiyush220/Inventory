package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.Stock;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {}
