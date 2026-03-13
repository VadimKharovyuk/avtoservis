package com.example.avtoservis.service;

import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.maper.ServiceItemMapper;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.repositoty.ServiceItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PublicServiceItemServiceImpl implements PublicServiceItemService {
    private final ServiceItemRepository serviceItemRepository;
    private final ServiceItemMapper serviceItemMapper;

    @Override
    @Transactional
    public ServiceItemResponseDto getBySlug(String slug) {
        ServiceItem entity = serviceItemRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new EntityNotFoundException("Služba nenalezena: " + slug));

        // Лічильник переглядів
        entity.setViews(entity.getViews() + 1);
        serviceItemRepository.save(entity);

        return serviceItemMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public List<ServiceItemResponseDto> getAllActive() {
        return serviceItemRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(serviceItemMapper::toResponse)
                .toList();
    }

}
