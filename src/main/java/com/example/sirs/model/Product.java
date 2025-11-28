package com.example.sirs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;
    private String unit;

    @Builder.Default
    private Integer leadTimeDays = 7;

    // Safety stock fields
    @Builder.Default
    private String safetyStockMethod = "percent"; // percent|statistical

    @Builder.Default
    private Double safetyStockValue = 10.0; // percent if method==percent

    @Builder.Default
    private Integer moq = 1;
}
