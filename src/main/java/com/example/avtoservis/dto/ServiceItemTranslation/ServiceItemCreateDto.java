package com.example.avtoservis.dto.ServiceItemTranslation;
import com.example.avtoservis.enums.ServiceCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemCreateDto {

    @NotNull(message = "Ціна обов'язкова")
    @DecimalMin(value = "0.0", message = "Ціна повинна бути додатньою")
    private BigDecimal price;

    @Builder.Default
    private boolean priceFrom = false;

    @NotNull(message = "Категорія обов'язкова")
    private ServiceCategory category;

    @Builder.Default
    private boolean active = true;

    // Переклади — мінімум один (CS)
    @Valid
    @Builder.Default
    private List<ServiceItemTranslationDto> translations = new ArrayList<>();
}
