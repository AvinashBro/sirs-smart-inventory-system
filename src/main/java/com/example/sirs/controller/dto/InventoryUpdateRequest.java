package com.example.sirs.controller.dto;

public class InventoryUpdateRequest {
    private Long productId;
    private Integer currentStock;
    private String location;

    public InventoryUpdateRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
