package com.example.sirs.service;

import com.example.sirs.model.*;
import com.example.sirs.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReorderService {

    private final ForecastService forecastService;
    private final InventoryRepository inventoryRepo;
    private final PurchaseOrderRepository poRepo;
    private final ProductRepository productRepo;

    @Transactional
    public void evaluateAndCreatePO(Inventory inv) {
        if (inv == null || inv.getProduct() == null) return;

        Product p = inv.getProduct();
        int leadTime = p.getLeadTimeDays() == null ? 7 : p.getLeadTimeDays();
        double avgDaily = forecastService.averageDailyDemand(p, 30);

        // Safety Stock Calculation
        int safetyStock = calculateSafetyStock(p, avgDaily, leadTime);

        int demandDuringLead = (int) Math.ceil(avgDaily * leadTime);
        int reorderPoint = demandDuringLead + safetyStock;

        inv.setReorderPoint(reorderPoint);
        inv.setLastForecastDate(LocalDate.now());
        inv.setLastUpdated(java.time.LocalDateTime.now());
        inventoryRepo.save(inv);

        int currentStock = inv.getCurrentStock() == null ? 0 : inv.getCurrentStock();
        int pendingQty = poRepo.findAll().stream()
                .filter(po -> "CREATED".equals(po.getStatus()) && po.getProduct().equals(p))
                .mapToInt(PurchaseOrder::getQty)
                .sum();

        int projectedStock = currentStock + pendingQty;

        if (projectedStock <= reorderPoint) {
            int eoq = (int) Math.ceil(avgDaily * 30); // 30-day coverage
            int qtyToOrder = Math.max(eoq - currentStock, p.getMoq() != null ? p.getMoq() : 1);

            PurchaseOrder po = PurchaseOrder.builder()
                    .product(p)
                    .qty(qtyToOrder)
                    .status("CREATED")
                    .vendor("Auto Supplier")
                    .eta(LocalDate.now().plusDays(leadTime))
                    .build();

            poRepo.save(po);

            log.warn("AUTO PURCHASE ORDER CREATED → SKU: {} | Qty: {} | Stock: {} | ROP: {} | AvgDaily: {:.2f}",
                    p.getSku(), qtyToOrder, currentStock, reorderPoint, avgDaily);
        }
    }

    private int calculateSafetyStock(Product p, double avgDaily, int leadTime) {
        if ("statistical".equalsIgnoreCase(p.getSafetyStockMethod())) {
            // You can implement MAD or StdDev later
            return (int) Math.ceil(avgDaily * leadTime * 0.3); // 30% buffer
        } else {
            // percent method
            double percent = p.getSafetyStockValue() == null ? 20.0 : p.getSafetyStockValue();
            return (int) Math.ceil((percent / 100.0) * avgDaily * leadTime);
        }
    }
}