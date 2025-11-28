package com.example.sirs.controller;

import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.service.ReorderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Profile("dev")                     // ← important
public class DebugController {

    private final ReorderService reorderService;
    private final InventoryRepository inventoryRepo;

    @GetMapping("/api/debug/run-reorder")
    public String runReorderNow() {
        inventoryRepo.findAll().forEach(reorderService::evaluateAndCreatePO);
        return "Reorder job executed for " + inventoryRepo.count() + " items!";
    }
}