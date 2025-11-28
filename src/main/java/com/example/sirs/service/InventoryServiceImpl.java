package com.example.sirs.service;

import com.example.sirs.model.Inventory;
import com.example.sirs.model.Product;
import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final ProductRepository productRepo;

    public InventoryServiceImpl(InventoryRepository inventoryRepo, ProductRepository productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public void updateStock(Long productId, int delta) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        Inventory inv = inventoryRepo.findByProductAndLocation(p, "MAIN")
                .orElseGet(() -> Inventory.builder().product(p).location("MAIN").currentStock(0).build());
        int newStock = (inv.getCurrentStock() == null ? 0 : inv.getCurrentStock()) + delta;
        inv.setCurrentStock(newStock);
        inventoryRepo.save(inv);
    }
}
