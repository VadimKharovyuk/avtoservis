package com.example.avtoservis.dto.ContactRequest;
import com.example.avtoservis.enums.RequestType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequestCreateDto {

    private String name;

    @NotBlank(message = "Телефон обов'язковий")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,20}$", message = "Невірний формат телефону")
    private String phone;

    @NotNull(message = "Тип заявки обов'язковий")
    private RequestType requestType;

    // ID послуги (якщо клієнт натиснув "Mám zájem")
    private Long serviceItemId;

    private String message;

    // Бажана дата та час запису
    @Future(message = "Дата запису повинна бути в майбутньому")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointmentDate;
}
