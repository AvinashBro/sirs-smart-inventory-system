// src/main/java/com/example/sirs/model/Inventory.java
package com.example.sirs.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    @Builder.Default
    private String location = "MAIN";

    @Builder.Default
    private Integer currentStock = 0;

    @Builder.Default
    private Integer reorderPoint = 0;

    private LocalDate lastForecastDate;

    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();
}