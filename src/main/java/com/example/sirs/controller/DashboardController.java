// src/main/java/com/example/sirs/controller/DashboardController.java
package com.example.sirs.controller;

import com.example.sirs.model.*;
import com.example.sirs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final InventoryRepository inventoryRepo;
    private final PurchaseOrderRepository poRepo;
    private final ProductRepository productRepo;
    private final SalesEventRepository salesEventRepo;

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Inventory> inventories = inventoryRepo.findAll();

        List<Inventory> lowStock = inventories.stream()
                .filter(i -> i.getCurrentStock() != null && i.getReorderPoint() != null)
                .filter(i -> i.getCurrentStock() <= i.getReorderPoint())
                .toList();

        var pendingPOs = poRepo.findAll().stream()
                .filter(po -> "CREATED".equals(po.getStatus()))
                .toList();

        model.addAttribute("inventories", inventories);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("pendingPOs", pendingPOs);

        return "dashboard";
    }

    @PostMapping("/seed-full")
    @ResponseBody
    public String seedFullData() {
        // Clear all old data
        poRepo.deleteAll();
        salesEventRepo.deleteAll();
        inventoryRepo.deleteAll();
        productRepo.deleteAll();

        // Seed 12+ realistic products
        List<Product> products = Arrays.asList(
                new Product(null, "MOUSE-001", "Wireless Mouse", "Electronics", 7, "Logitech", 2.5, 20),
                new Product(null, "CABLE-002", "USB-C Cable 2m", "Accessories", 5, "Anker", 5.0, 15),
                new Product(null, "CHARGER-003", "65W GAN Charger", "Electronics", 3, "Anker", 1.2, 25),
                new Product(null, "STAND-004", "Laptop Stand", "Furniture", 10, "Amazon Basics", 3.0, 20),
                new Product(null, "WEBCAM-005", "Webcam 1080p", "Electronics", 14, "Logitech", 0.8, 30),
                new Product(null, "KEYBOARD-006", "Mechanical Keyboard", "Electronics", 8, "Keychron", 1.5, 20),
                new Product(null, "EARBUD-007", "Bluetooth Earbuds", "Audio", 4, "Sony", 4.5, 20),
                new Product(null, "MONITOR-008", "27\" Monitor", "Electronics", 21, "Dell", 0.7, 25),
                new Product(null, "SSD-009", "1TB NVMe SSD", "Storage", 6, "Samsung", 2.2, 20),
                new Product(null, "PAD-010", "RGB Mouse Pad", "Accessories", 9, "Corsair", 4.0, 15),
                new Product(null, "HEADPHONE-011", "Noise-Cancelling Headphones", "Audio", 12, "Bose", 1.3, 25),
                new Product(null, "ROUTER-012", "WiFi 6 Router", "Networking", 15, "TP-Link", 0.9, 20)
        );

        productRepo.saveAll(products);

        // Create inventory with realistic stock
        products.forEach(p -> {
            Inventory inv = new Inventory();
            inv.setProduct(p);
            inv.setCurrentStock(20 + (int)(Math.random() * 120)); // Some will be low
            inv.setReorderPoint(0);
            inventoryRepo.save(inv);
        });

        // Generate 50 days of sales → will trigger low stock naturally
        LocalDate today = LocalDate.now();
        for (int day = 0; day < 50; day++) {
            LocalDateTime saleTime = today.minusDays(day).atStartOfDay();
            for (Product p : products) {
                if (Math.random() > 0.35) {
                    int qty = 1 + (int)(Math.random() * 10);

                    SalesEvent event = new SalesEvent();
                    event.setProduct(p);
                    event.setQuantity(qty);
                    event.setSaleTime(saleTime);
                    event.setSource("WEB");
                    salesEventRepo.save(event);

                    // Manually reduce stock (since no service call)
                    Inventory inv = inventoryRepo.findByProduct(p).orElse(null);
                    if (inv != null) {
                        inv.setCurrentStock(inv.getCurrentStock() - qty);
                        if (inv.getCurrentStock() < 0) inv.setCurrentStock(0);
                        inventoryRepo.save(inv);
                    }
                }
            }
        }

        return """
            <div style="text-align:center; margin-top:100px; font-family:Arial; color:#333">
                <h1 style="color:green; font-size:48px">SUCCESS!</h1>
                <h2>12+ Products + 50 Days Sales Data Loaded</h2>
                <h3 style="color:red">Red alerts & low stock now visible!</h3>
                <br><br>
                <a href="/" style="font-size:28px; color:#0066ff; text-decoration:none">
                    ← CLICK HERE TO SEE YOUR FULL DASHBOARD
                </a>
            </div>
            """;
    }
}