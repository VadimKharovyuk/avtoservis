package com.example.avtoservis.service;

import com.example.avtoservis.dto.ServiceItemResponseDto;

import java.util.List;

public interface PublicServiceItemService {

// === Інтерфейс — додати методи ===

    ServiceItemResponseDto getBySlug(String slug);

    List<ServiceItemResponseDto> getAllActive();
}
