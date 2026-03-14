package com.example.avtoservis.service;

import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.enums.Language;

import java.util.List;

public interface PublicServiceItemService {

// === Інтерфейс — додати методи ===

    ServiceItemResponseDto getBySlug(String slug, Language language);

    List<ServiceItemResponseDto> getAllActive();
}
