package com.example.avtoservis.maper;
import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemTranslation.ServiceItemTranslationDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.model.ServiceItemTranslation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceItemMapper {

    // ===================== ServiceItem =====================

    public ServiceItem toEntity(ServiceItemCreateDto dto) {
        ServiceItem entity = ServiceItem.builder()
                .price(dto.getPrice())
                .priceFrom(dto.isPriceFrom())
                .category(dto.getCategory())
                .active(dto.isActive())
                .build();

        if (dto.getTranslations() != null) {
            dto.getTranslations().stream()
                    .filter(tDto -> tDto.getName() != null && !tDto.getName().isBlank())
                    .forEach(tDto -> entity.addTranslation(toTranslationEntity(tDto)));
        }

        return entity;
    }

    public void updateEntity(ServiceItem entity, ServiceItemUpdateDto dto) {
        entity.setPrice(dto.getPrice());
        entity.setPriceFrom(dto.isPriceFrom());
        entity.setCategory(dto.getCategory());
        entity.setActive(dto.isActive());

        if (dto.getTranslations() != null) {
            dto.getTranslations().stream()
                    .filter(tDto -> tDto.getName() != null && !tDto.getName().isBlank())
                    .forEach(tDto ->
                            entity.getTranslation(tDto.getLanguage())
                                    .ifPresentOrElse(
                                            existing -> updateTranslationEntity(existing, tDto),
                                            () -> entity.addTranslation(toTranslationEntity(tDto))
                                    ));
        }
    }

//    // Для адмінки — всі переклади
//    public ServiceItemResponseDto toResponse(ServiceItem entity) {
//        return ServiceItemResponseDto.builder()
//                .id(entity.getId())
//                .price(entity.getPrice())
//                .priceFrom(entity.isPriceFrom())
//                .category(entity.getCategory())
//                .categoryDisplayName(entity.getCategory().getDisplayName())
//                .imageUrl(entity.getImageUrl())
//                .views(entity.getViews())
//                .active(entity.isActive())
//                .createdAt(entity.getCreatedAt())
//                .translations(toTranslationDtoList(entity.getTranslations()))
//                .build();
//    }
public ServiceItemResponseDto toResponse(ServiceItem entity) {
    ServiceItemTranslation uk = entity.getTranslationOrDefault(Language.UK);

    return ServiceItemResponseDto.builder()
            .id(entity.getId())
            .name(uk != null ? uk.getName() : "—")
            .slug(uk != null ? uk.getSlug() : null)
            .price(entity.getPrice())
            .priceFrom(entity.isPriceFrom())
            .category(entity.getCategory())
            .categoryDisplayName(entity.getCategory().getDisplayName())
            .imageUrl(entity.getImageUrl())
            .views(entity.getViews())
            .active(entity.isActive())
            .createdAt(entity.getCreatedAt())
            .translations(toTranslationDtoList(entity.getTranslations()))
            .build();
}

    // Для публічної частини — одна мова
    public ServiceItemResponseDto toResponse(ServiceItem entity, Language language) {
        ServiceItemTranslation t = entity.getTranslationOrDefault(language);

        ServiceItemResponseDto.ServiceItemResponseDtoBuilder builder = ServiceItemResponseDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .priceFrom(entity.isPriceFrom())
                .category(entity.getCategory())
                .categoryDisplayName(entity.getCategory().getDisplayName())
                .imageUrl(entity.getImageUrl())
                .views(entity.getViews())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt());

        if (t != null) {
            builder.name(t.getName())
                    .description(t.getDescription())
                    .slug(t.getSlug())
                    .metaTitle(t.getMetaTitle())
                    .metaDescription(t.getMetaDescription())
                    .imageAlt(t.getImageAlt());
        }

        return builder.build();
    }

    // ===================== Translation =====================

    private ServiceItemTranslation toTranslationEntity(ServiceItemTranslationDto dto) {
        return ServiceItemTranslation.builder()
                .language(dto.getLanguage())
                .name(blankToNull(dto.getName()))
                .description(blankToNull(dto.getDescription()))
                .slug(blankToNull(dto.getSlug()))
                .metaTitle(blankToNull(dto.getMetaTitle() != null ? dto.getMetaTitle() : dto.getName()))
                .metaDescription(blankToNull(dto.getMetaDescription()))
                .imageAlt(blankToNull(dto.getImageAlt()))
                .build();
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private void updateTranslationEntity(ServiceItemTranslation entity, ServiceItemTranslationDto dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        // Slug можна задати тільки якщо ще не було
        if (entity.getSlug() == null && dto.getSlug() != null && !dto.getSlug().isBlank()) {
            entity.setSlug(dto.getSlug().trim());
        }

        entity.setMetaTitle(dto.getMetaTitle());
        entity.setMetaDescription(dto.getMetaDescription());
        entity.setImageAlt(dto.getImageAlt());
    }

    private List<ServiceItemTranslationDto> toTranslationDtoList(List<ServiceItemTranslation> translations) {
        if (translations == null) return List.of();
        return translations.stream()
                .map(this::toTranslationDto)
                .toList();
    }

    private ServiceItemTranslationDto toTranslationDto(ServiceItemTranslation entity) {
        return ServiceItemTranslationDto.builder()
                .language(entity.getLanguage())
                .name(entity.getName())
                .description(entity.getDescription())
                .slug(entity.getSlug())
                .metaTitle(entity.getMetaTitle())
                .metaDescription(entity.getMetaDescription())
                .imageAlt(entity.getImageAlt())
                .build();
    }
}