// src/main/java/com/example/sirs/controller/DashboardController.java
package com.example.sirs.controller;

import com.example.sirs.model.Inventory;
import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final InventoryRepository inventoryRepo;
    private final PurchaseOrderRepository poRepo;

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
}