package com.example.avtoservis.maper;
import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.model.ServiceItem;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class ServiceItemMapper {

    public ServiceItem toEntity(ServiceItemCreateDto dto) {
        return ServiceItem.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .priceFrom(dto.isPriceFrom())
                .category(dto.getCategory())
                .slug(dto.getSlug())
                .metaTitle(dto.getMetaTitle() != null ? dto.getMetaTitle() : dto.getName())
                .metaDescription(dto.getMetaDescription())
                .imageAlt(dto.getImageAlt())
                .active(dto.isActive())
                .build();
    }

    public void updateEntity(ServiceItem entity, ServiceItemUpdateDto dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setPriceFrom(dto.isPriceFrom());
        entity.setCategory(dto.getCategory());
        // slug НЕ обновляем — проиндексирован
        entity.setMetaTitle(dto.getMetaTitle());
        entity.setMetaDescription(dto.getMetaDescription());
        entity.setImageAlt(dto.getImageAlt());
        entity.setActive(dto.isActive());
    }

    public ServiceItemResponseDto toResponse(ServiceItem entity) {
        return ServiceItemResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .priceFrom(entity.isPriceFrom())
                .category(entity.getCategory())
                .categoryDisplayName(entity.getCategory().getDisplayName())
                .slug(entity.getSlug())
                .metaTitle(entity.getMetaTitle())
                .metaDescription(entity.getMetaDescription())
                .imageUrl(entity.getImageUrl())
                .imageAlt(entity.getImageAlt())
                .views(entity.getViews())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }


}
