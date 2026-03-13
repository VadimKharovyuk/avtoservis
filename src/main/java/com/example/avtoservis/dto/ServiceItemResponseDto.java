package com.example.avtoservis.dto;
import com.example.avtoservis.enums.ServiceCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemResponseDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean priceFrom;
    private ServiceCategory category;
    private String categoryDisplayName;

    // SEO
    private String slug;
    private String metaTitle;
    private String metaDescription;

    // Изображение
    private String imageUrl;
    private String imageAlt;

    private Integer views;
    private boolean active;
    private LocalDateTime createdAt;
}
