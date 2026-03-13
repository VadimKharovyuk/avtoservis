package com.example.avtoservis.dto.ContactRequest;
import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.enums.RequestType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequestResponseDto {

    private Long id;
    private String name;
    private String phone;
    private RequestType requestType;
    private String requestTypeDisplayName;
    private RequestStatus status;
    private String statusDisplayName;
    private String message;

    // Послуга
    private Long serviceItemId;
    private String serviceItemName;

    // Дати
    private LocalDateTime appointmentDate;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
}
