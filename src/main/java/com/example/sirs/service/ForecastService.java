package com.example.sirs.service;

import com.example.sirs.model.Product;
import com.example.sirs.model.SalesEvent;
import com.example.sirs.repository.SalesEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    private final SalesEventRepository salesRepo;

    public ForecastService(SalesEventRepository salesRepo) {
        this.salesRepo = salesRepo;
    }

    // average daily demand over last N days
    public double averageDailyDemand(Product product, int days) {
        LocalDateTime from = LocalDate.now().minusDays(days).atStartOfDay();
        List<SalesEvent> events = salesRepo.findByProductAndSaleTimeAfter(product, from);

        Map<LocalDate, Integer> perDay = events.stream()
                .collect(Collectors.groupingBy(e -> e.getSaleTime().toLocalDate(),
                        Collectors.summingInt(  SalesEvent::getQuantity)));

        int total = perDay.values().stream().mapToInt(Integer::intValue).sum();
        return (double) total / Math.max(1, days);
    }
}
