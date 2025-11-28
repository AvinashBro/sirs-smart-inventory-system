package com.example.sirs.controller;

import com.example.sirs.model.Inventory;
import com.example.sirs.model.Product;
import com.example.sirs.model.SalesEvent;
import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.repository.ProductRepository;
import com.example.sirs.repository.SalesEventRepository;
import com.example.sirs.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SalesController {

    private final ProductRepository productRepo;
    private final SalesEventRepository salesEventRepo;
    private final InventoryRepository inventoryRepo;
    private final InventoryService inventoryService;

    @PostMapping("/api/sales/event")
    public ResponseEntity<String> ingest(@RequestBody Map<String, Object> payload) {
        try {
            Long productId = Long.valueOf(payload.get("productId").toString());
            Integer quantity = Integer.valueOf(payload.get("quantity").toString());
            String source = payload.getOrDefault("source", "api").toString();

            // 1. Find product
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            // 2. Save sales event
            SalesEvent event = SalesEvent.builder()
                    .product(product)
                    .quantity(quantity)
                    .saleTime(LocalDateTime.now())
                    .source(source)
                    .build();
            salesEventRepo.save(event);

            // 3. Deduct from stock using your existing service
            inventoryService.updateStock(productId, -quantity);

            return ResponseEntity.ok("Sale recorded & stock updated for SKU: " + product.getSku());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}