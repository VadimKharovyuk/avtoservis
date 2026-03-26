package com.example.avtoservis.admin.service.impl;
import com.example.avtoservis.admin.service.AdminServiceItemService;
import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.maper.ServiceItemMapper;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.repositoty.ServiceItemRepository;
import com.example.avtoservis.util.PageResponse;
import com.example.avtoservis.util.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceItemServiceImpl implements AdminServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final ServiceItemMapper serviceItemMapper;
    private final StorageService storageService;



    @Override
    @Transactional
    public ServiceItemResponseDto create(ServiceItemCreateDto createDto) {
        log.info("Creating service item, category: {}", createDto.getCategory());

        ServiceItem entity = serviceItemMapper.toEntity(createDto);
        ServiceItem saved = serviceItemRepository.save(entity);

        log.info("Service item created with id: {}", saved.getId());
        return serviceItemMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ServiceItemResponseDto update(Long id, ServiceItemUpdateDto updateDto) {
        log.info("Updating service item id: {}", id);

        ServiceItem entity = findEntityById(id);
        serviceItemMapper.updateEntity(entity, updateDto);
        ServiceItem saved = serviceItemRepository.save(entity);

        log.info("Service item updated: {}", saved.getId());
        return serviceItemMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ServiceItemResponseDto uploadPhoto(MultipartFile file, Long serviceItemId) {
        log.info("Uploading photo for service item id: {}", serviceItemId);

        ServiceItem entity = findEntityById(serviceItemId);

        if (entity.getImageId() != null) {
            log.info("Deleting old photo: {}", entity.getImageId());
            storageService.deleteImage(entity.getImageId());
        }

        try {
            StorageService.StorageResult result = storageService.uploadImage(file);
            entity.setImageUrl(result.getUrl());
            entity.setImageId(result.getImageId());
            ServiceItem saved = serviceItemRepository.save(entity);

            log.info("Photo uploaded for service item id: {}", serviceItemId);
            return serviceItemMapper.toResponse(saved);

        } catch (IOException e) {
            log.error("Failed to upload photo for service item id: {}", serviceItemId, e);
            throw new RuntimeException("Failed to upload photo: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ServiceItemResponseDto deletePhoto(Long serviceItemId) {
        log.info("Deleting photo for service item id: {}", serviceItemId);

        ServiceItem entity = findEntityById(serviceItemId);

        if (entity.getImageId() != null) {
            storageService.deleteImage(entity.getImageId());
            entity.setImageUrl(null);
            entity.setImageId(null);
            serviceItemRepository.save(entity);
            log.info("Photo deleted for service item id: {}", serviceItemId);
        }

        return serviceItemMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceItemResponseDto getById(Long id) {
        return serviceItemMapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ServiceItemResponseDto> getAll(Pageable pageable) {
        Page<ServiceItem> entityPage = serviceItemRepository.findAllWithTranslations(pageable);
        Page<ServiceItemResponseDto> page = entityPage.map(serviceItemMapper::toResponse);
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting service item id: {}", id);

        ServiceItem entity = findEntityById(id);

        if (entity.getImageId() != null) {
            storageService.deleteImage(entity.getImageId());
        }

        serviceItemRepository.delete(entity);
        log.info("Service item deleted: {}", id);
    }


    @Value("${app.home.services-limit:3}")
    private int servicesLimit;

    // Для публічної частини — з мовою
    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemResponseDto> getLatestServices(Language language) {
        return serviceItemRepository
                .findByActiveTrueOrderByCreatedAtDesc(PageRequest.of(0, servicesLimit))
                .stream()
                .map(entity -> serviceItemMapper.toResponse(entity, language))
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


    private ServiceItem findEntityById(Long id) {
        return serviceItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Service item not found with id: " + id));
    }
}