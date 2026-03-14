package com.example.avtoservis.service;

import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.maper.ServiceItemMapper;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.model.ServiceItemTranslation;
import com.example.avtoservis.repositoty.ServiceItemRepository;
import com.example.avtoservis.repositoty.ServiceItemTranslationRepository;
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
    private final ServiceItemTranslationRepository serviceItemTranslationRepository ;

    @Override
    @Transactional
    public ServiceItemResponseDto getBySlug(String slug, Language language) {
        ServiceItemTranslation translation = serviceItemTranslationRepository
                .findBySlugAndLanguage(slug, language)
                .orElseThrow(() -> new EntityNotFoundException("Služba nenalezena: " + slug));

        ServiceItem entity = translation.getServiceItem();

        // Лічильник переглядів
        entity.setViews(entity.getViews() + 1);
        serviceItemRepository.save(entity);

        return serviceItemMapper.toResponse(entity, language);
    }

    @Override
    @Transactional
    public List<ServiceItemResponseDto> getAllActive() {
        return serviceItemRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(serviceItemMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public List<ServiceItemResponseDto> getAllActive(Language language) {
        return serviceItemRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(entity -> serviceItemMapper.toResponse(entity, language))
                .toList();
    }


}
