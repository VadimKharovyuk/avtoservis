package com.example.avtoservis.admin.service;


// === Інтерфейс ===

import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.dto.ContactRequest.ContactRequestResponseDto;
import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.util.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AdminContactRequestService {

    ContactRequestResponseDto create(ContactRequestCreateDto dto);

    ContactRequestResponseDto updateStatus(Long id, RequestStatus status);

    ContactRequestResponseDto getById(Long id);

    PageResponse<ContactRequestResponseDto> getAll(Pageable pageable);

    PageResponse<ContactRequestResponseDto> getByStatus(RequestStatus status, Pageable pageable);

    void deleteById(Long id);

    long countNew();
}
