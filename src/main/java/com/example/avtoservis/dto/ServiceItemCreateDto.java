package com.example.avtoservis.dto;
import com.example.avtoservis.enums.ServiceCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemCreateDto {

    @NotBlank(message = "Název služby je povinný")
    private String name;

    private String description;

    @NotNull(message = "Cena je povinná")
    @DecimalMin(value = "0.0", message = "Cena musí být kladná")
    private BigDecimal price;

    @Builder.Default
    private boolean priceFrom = false;

    @NotNull(message = "Kategorie je povinná")
    private ServiceCategory category;

    // --- SEO (необязательные, сгенерируются автоматически если пустые) ---
    private String slug;

    @Size(max = 160, message = "Meta title max 160 znaků")
    private String metaTitle;

    @Size(max = 300, message = "Meta description max 300 znaků")
    private String metaDescription;

    @Size(max = 500, message = "Alt text max 500 znaků")
    private String imageAlt;

    @Builder.Default
    private boolean active = true;
}
