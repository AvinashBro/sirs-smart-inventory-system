// src/main/java/com/example/sirs/model/Product.java
package com.example.sirs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

    @Builder.Default
    private String safetyStockMethod = "percent";

    @Builder.Default
    private Double safetyStockValue = 20.0;

    @Builder.Default
    private Integer moq = 1;
}