// src/main/java/com/example/sirs/config/DataInitializer.java
package com.example.sirs.config;

import com.example.sirs.model.*;
import com.example.sirs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final InventoryRepository inventoryRepo;
    private final SalesEventRepository salesRepo;

    @Override
    public void run(String... args) {
        if (productRepo.count() == 0) {
            Product mouse = Product.builder()
                    .sku("MOUSE-001")
                    .name("Wireless Mouse")
                    .leadTimeDays(5)
                    .safetyStockValue(30.0)
                    .moq(50)
                    .build();
            productRepo.save(mouse);

            Inventory inv = Inventory.builder()
                    .product(mouse)
                    .currentStock(12)
                    .location("MAIN")
                    .build();
            inventoryRepo.save(inv);

            // Add 25 days of sales
            for (int i = 1; i <= 25; i++) {
                SalesEvent sale = SalesEvent.builder()
                        .product(mouse)
                        .quantity(3 + (i % 5))
                        .saleTime(LocalDateTime.now().minusDays(i))
                        .source("POS")
                        .build();
                salesRepo.save(sale);
            }

            System.out.println("Sample data created: MOUSE-001 with low stock!");
        }
    }
}