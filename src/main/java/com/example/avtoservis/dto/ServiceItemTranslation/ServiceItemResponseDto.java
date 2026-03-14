package com.example.avtoservis.dto.ServiceItemTranslation;
import com.example.avtoservis.enums.ServiceCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemResponseDto {

    private Long id;
    private BigDecimal price;
    private boolean priceFrom;
    private ServiceCategory category;
    private String categoryDisplayName;

    private String imageUrl;

    private Integer views;
    private boolean active;
    private LocalDateTime createdAt;

    // Всі переклади (для адмінки)
    private List<ServiceItemTranslationDto> translations;

    // Поточна мова (для публічної частини)
    private String name;
    private String description;
    private String slug;
    private String metaTitle;
    private String metaDescription;
    private String imageAlt;
}
