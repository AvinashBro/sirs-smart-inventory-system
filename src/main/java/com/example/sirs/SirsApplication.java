package com.example.sirs;

import com.example.sirs.model.*;
import com.example.sirs.repository.*;
import com.example.sirs.service.ReorderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class SirsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SirsApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ProductRepository productRepo,
                               InventoryRepository inventoryRepo,
                               SalesEventRepository salesRepo,
                               PurchaseOrderRepository poRepo,
                               ReorderService reorderService,
                               Environment env) {
        return args -> {
            // Run auto-seed only on Render or when dev profile is active
            if (env.getActiveProfiles().length == 0 || Arrays.asList(env.getActiveProfiles()).contains("dev")) {
                if (productRepo.count() == 0) {  // Only seed if DB is empty
                    System.out.println("=== AUTO SEEDING FULL DATA ON STARTUP ===");
                    // (Paste the exact same seeding code from /seed-full here)
                    // ... same 15 products + sales + reorderService.evaluateAndCreatePOForAll()
                    // I can give you the full code if you want – just say “auto”
                }
            }
        };
    }
}