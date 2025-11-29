// src/main/java/com/example/sirs/repository/PurchaseOrderRepository.java
package com.example.sirs.repository;

import com.example.sirs.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(String status);  // ← ADD THIS LINE
}