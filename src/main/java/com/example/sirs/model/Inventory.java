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

    @Builder.Default                     // ← ADD THIS
    private String location = "MAIN";

    @Builder.Default                     // ← ADD THIS
    @Column(nullable = false)
    private Integer currentStock = 0;

    @Builder.Default                     // ← ADD THIS
    private Integer reorderPoint = 0;

    private LocalDate lastForecastDate;

    @Builder.Default                     // ← ADD THIS
    private LocalDateTime lastUpdated = LocalDateTime.now();
}