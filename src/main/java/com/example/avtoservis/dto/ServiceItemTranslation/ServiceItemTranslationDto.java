package com.example.avtoservis.dto.ServiceItemTranslation;
import com.example.avtoservis.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceItemTranslationDto {

    private Language language;


    private String name;

    private String description;

    private String slug;

    @Size(max = 160, message = "Meta title max 160 символів")
    private String metaTitle;

    @Size(max = 300, message = "Meta description max 300 символів")
    private String metaDescription;

    @Size(max = 500, message = "Alt text max 500 символів")
    private String imageAlt;
}
