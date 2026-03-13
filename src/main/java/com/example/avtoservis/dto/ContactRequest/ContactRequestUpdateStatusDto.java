package com.example.avtoservis.dto.ContactRequest;

import com.example.avtoservis.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestUpdateStatusDto {

    @NotNull(message = "Статус обов'язковий")
    private RequestStatus status;
}
