package com.example.avtoservis.maper;

import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.dto.ContactRequest.ContactRequestResponseDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.model.ContactRequest;
import com.example.avtoservis.model.ServiceItem;
import com.example.avtoservis.model.ServiceItemTranslation;
import org.springframework.stereotype.Component;

@Component
public class ContactRequestMapper {

    public ContactRequest toEntity(ContactRequestCreateDto dto, ServiceItem serviceItem) {
        return ContactRequest.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .requestType(dto.getRequestType())
                .serviceItem(serviceItem)
                .message(dto.getMessage())
                .appointmentDate(dto.getAppointmentDate())
                .build();
    }

    public ContactRequestResponseDto toResponse(ContactRequest entity) {
        String serviceItemName = null;
        if (entity.getServiceItem() != null) {
            ServiceItemTranslation t = entity.getServiceItem().getTranslationOrDefault(Language.CS);
            serviceItemName = (t != null) ? t.getName() : null;
        }

        return ContactRequestResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .requestType(entity.getRequestType())
                .requestTypeDisplayName(entity.getRequestType().getDisplayName())
                .status(entity.getStatus())
                .statusDisplayName(entity.getStatus().getDisplayName())
                .message(entity.getMessage())
                .serviceItemId(entity.getServiceItem() != null ? entity.getServiceItem().getId() : null)
                .serviceItemName(serviceItemName)
                .appointmentDate(entity.getAppointmentDate())
                .processedAt(entity.getProcessedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
