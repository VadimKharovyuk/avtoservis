package com.example.avtoservis.admin.service;

import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.util.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface AdminServiceItemService {

    ServiceItemResponseDto create(ServiceItemCreateDto createDto);

    ServiceItemResponseDto update(Long id, ServiceItemUpdateDto updateDto);

    ServiceItemResponseDto uploadPhoto(MultipartFile file, Long serviceItemId);

    ServiceItemResponseDto deletePhoto(Long serviceItemId);

    ServiceItemResponseDto getById(Long id);

    PageResponse<ServiceItemResponseDto> getAll(Pageable pageable);

    void deleteById(Long id);



    List<ServiceItemResponseDto> getLatestServices();
    List<ServiceItemResponseDto> getLatestServices(Language language);
}
