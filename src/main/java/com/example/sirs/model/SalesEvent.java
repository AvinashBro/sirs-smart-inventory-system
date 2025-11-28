package com.example.sirs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sales_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private LocalDateTime saleTime;
    private String source;
}
