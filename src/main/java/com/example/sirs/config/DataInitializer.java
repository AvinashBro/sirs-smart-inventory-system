package com.example.sirs.config;

import com.example.sirs.model.*;
import com.example.sirs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final InventoryRepository invRepo;
    private final SalesEventRepository salesRepo;

    @Override
    public void run(String... args) throws Exception {
        if (productRepo.count() == 0) {
            System.out.println("No products found → Seeding sample data...");

            // Create sample product
            Product mouse = Product.builder()
                    .sku("MOUSE-001")
                    .name("Wireless Mouse")
                    .leadTimeDays(5)
                    .safetyStockMethod("percent")
                    .safetyStockValue(30.0)
                    .moq(50)
                    .build();
            productRepo.save(mouse);

            // Create inventory with low stock (to trigger reorder)
            Inventory inv = Inventory.builder()
                    .product(mouse)
                    .currentStock(12)        // ← Very low → will trigger auto PO
                    .location("MAIN")
                    .build();
            invRepo.save(inv);

            // Simulate 25 days of sales (for accurate forecast)
            for (int i = 1; i <= 25; i++) {
                SalesEvent sale = SalesEvent.builder()
                        .product(mouse)
                        .quantity(3 + (i % 5))  // randomish: 3 to 7 units/day
                        .saleTime(java.time.LocalDateTime.now().minusDays(i))
                        .source("POS")
                        .build();
                salesRepo.save(sale);
            }

            System.out.println("Sample data seeded! Product: MOUSE-001 | Stock: 12");
        }
    }
}