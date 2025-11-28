package com.example.sirs.service;

public interface InventoryService {
    void updateStock(Long productId, int delta);
}
