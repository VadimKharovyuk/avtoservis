package com.example.avtoservis.admin.service.impl;
import com.example.avtoservis.admin.service.AdminContactRequestService;
import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.dto.ContactRequest.ContactRequestResponseDto;
import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.maper.ContactRequestMapper;
import com.example.avtoservis.model.ContactRequest;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.repositoty.ContactRequestRepository;
import com.example.avtoservis.repositoty.ServiceItemRepository;
import com.example.avtoservis.util.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminContactRequestServiceImpl implements AdminContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final ContactRequestMapper contactRequestMapper;


    @Override
    @Transactional
    public ContactRequestResponseDto create(ContactRequestCreateDto dto) {
        log.info("Нова заявка: {} — {}", dto.getPhone(), dto.getRequestType());

        // Знаходимо послугу якщо вказана
        ServiceItem serviceItem = null;
        if (dto.getServiceItemId() != null) {
            serviceItem = serviceItemRepository.findById(dto.getServiceItemId())
                    .orElse(null);
        }

        ContactRequest entity = contactRequestMapper.toEntity(dto, serviceItem);
        ContactRequest saved = contactRequestRepository.save(entity);

        log.info("Заявка створена id: {}", saved.getId());
        return contactRequestMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ContactRequestResponseDto updateStatus(Long id, RequestStatus status) {
        log.info("Зміна статусу заявки id: {} на {}", id, status);

        ContactRequest entity = findEntityById(id);
        entity.setStatus(status);

        // Фіксуємо дату обробки
        if (status == RequestStatus.DONE && entity.getProcessedAt() == null) {
            entity.setProcessedAt(LocalDateTime.now());
        }

        ContactRequest saved = contactRequestRepository.save(entity);
        return contactRequestMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactRequestResponseDto getById(Long id) {
        return contactRequestMapper.toResponse(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactRequestResponseDto> getAll(Pageable pageable) {
        Page<ContactRequest> page = contactRequestRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PageResponse.from(page.map(contactRequestMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactRequestResponseDto> getByStatus(RequestStatus status, Pageable pageable) {
        Page<ContactRequest> page = contactRequestRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return PageResponse.from(page.map(contactRequestMapper::toResponse));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Видалення заявки id: {}", id);
        ContactRequest entity = findEntityById(id);
        contactRequestRepository.delete(entity);
    }

    @Override
    public long countNew() {
        return contactRequestRepository.countByStatus(RequestStatus.NEW);
    }

    private ContactRequest findEntityById(Long id) {
        return contactRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Contact request not found with id: " + id));
    }
}
