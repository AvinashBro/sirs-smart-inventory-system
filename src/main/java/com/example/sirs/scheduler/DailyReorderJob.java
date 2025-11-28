package com.example.sirs.scheduler;

import com.example.sirs.repository.InventoryRepository;
import com.example.sirs.service.ReorderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyReorderJob {

    private final InventoryRepository inventoryRepo;
    private final ReorderService reorderService;

    public DailyReorderJob(InventoryRepository inventoryRepo, ReorderService reorderService) {
        this.inventoryRepo = inventoryRepo;
        this.reorderService = reorderService;
    }

    // run every day at 3:00 AM local time (cron)
    @Scheduled(cron = "0 0 3 * * *")
    public void run() {
        inventoryRepo.findAll().forEach(inv -> {
            try {
                reorderService.evaluateAndCreatePO(inv);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
