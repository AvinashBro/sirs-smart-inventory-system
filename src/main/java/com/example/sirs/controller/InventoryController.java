package com.example.sirs.controller;

import com.example.sirs.controller.dto.InventoryUpdateRequest;
import com.example.sirs.model.Inventory;
import com.example.sirs.model.Product;
import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.repository.ProductRepository;
import com.example.sirs.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepo;
    private final ProductRepository productRepo;
    private final InventoryService inventoryService;

    public InventoryController(InventoryRepository inventoryRepo,
                               ProductRepository productRepo,
                               InventoryService inventoryService) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> all() {
        return inventoryRepo.findAll();
    }

    @PostMapping("/update")
    public ResponseEntity<Inventory> update(@RequestBody InventoryUpdateRequest dto) {
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

        String loc = dto.getLocation() == null ? "MAIN" : dto.getLocation();

        Inventory inv = inventoryRepo.findByProductAndLocation(p, loc)
                .orElseGet(() -> Inventory.builder()
                        .product(p)
                        .location(loc)
                        .currentStock(0)
                        .build());

        inv.setCurrentStock(dto.getCurrentStock());
        inv.setLocation(loc);

        Inventory saved = inventoryRepo.save(inv);
        return ResponseEntity.ok(saved);
    }

    // adjust stock by delta
    @PostMapping("/adjust")
    public ResponseEntity<String> adjust(@RequestBody InventoryAdjustRequest adjust) {
        inventoryService.updateStock(adjust.getProductId(), adjust.getDelta());
        return ResponseEntity.ok("OK");
    }

    public static class InventoryAdjustRequest {
        private Long productId;
        private int delta;
        public InventoryAdjustRequest() {}
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getDelta() { return delta; }
        public void setDelta(int delta) { this.delta = delta; }
    }
}
