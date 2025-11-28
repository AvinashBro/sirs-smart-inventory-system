package com.example.sirs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;   // ← THIS LINE WAS MISSING

@SpringBootApplication
@EnableScheduling   // ← This enables your @Scheduled job at 3 AM
public class SirsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SirsApplication.class, args);
    }
}
