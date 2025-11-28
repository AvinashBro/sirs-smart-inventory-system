package com.example.sirs.repository;

import com.example.sirs.model.SalesEvent;
import com.example.sirs.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesEventRepository extends JpaRepository<SalesEvent, Long> {
    List<SalesEvent> findByProductAndSaleTimeAfter(Product product, LocalDateTime after);
}
