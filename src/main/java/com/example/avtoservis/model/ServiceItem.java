package com.example.avtoservis.model;

import com.example.avtoservis.enums.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "service_items")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Builder.Default
    private boolean priceFrom = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;

    // --- SEO ---
    @Column(unique = true)
    private String slug;  // "vymena-motoroveho-oleje" для URL /sluzby/vymena-motoroveho-oleje

    @Column(length = 160)
    private String metaTitle;  // <title> для страницы услуги

    @Column(length = 300)
    private String metaDescription;  // <meta name="description">

    // --- Изображения ---
    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String imageId;

    @Column(length = 500)
    private String imageAlt;  // alt-текст для SEO изображений


    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }


}