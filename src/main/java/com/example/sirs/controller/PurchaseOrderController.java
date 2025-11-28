package com.example.sirs.controller;

import com.example.sirs.model.PurchaseOrder;
import com.example.sirs.repository.PurchaseOrderRepository;
import com.example.sirs.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/po")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderRepository poRepo;
    private final InventoryService inventoryService;

    @PostMapping("/{id}/receive")
    public ResponseEntity<String> receive(@PathVariable Long id) {
        PurchaseOrder po = poRepo.findById(id).orElseThrow();
        if (!"CREATED".equals(po.getStatus())) {
            return ResponseEntity.badRequest().body("Already received or invalid");
        }

        inventoryService.updateStock(po.getProduct().getId(), po.getQty());
        po.setStatus("RECEIVED");
        poRepo.save(po);

        return ResponseEntity.ok("Stock updated and PO marked as received");
    }
}