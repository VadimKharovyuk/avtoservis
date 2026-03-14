package com.example.avtoservis.model;

import com.example.avtoservis.enums.Language;
import com.example.avtoservis.enums.ServiceCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "service_items")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Builder.Default
    private boolean priceFrom = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;



    // --- Изображения ---
    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String imageId;


    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }

    // --- Переклади ---
    @OneToMany(mappedBy = "serviceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ServiceItemTranslation> translations = new ArrayList<>();

    // --- Хелпери ---
    public Optional<ServiceItemTranslation> getTranslation(Language language) {
        return translations.stream()
                .filter(t -> t.getLanguage() == language)
                .findFirst();
    }

    public ServiceItemTranslation getTranslationOrDefault(Language language) {
        return getTranslation(language)
                .orElseGet(() -> getTranslation(Language.CS)
                        .orElse(null));
    }

    public void addTranslation(ServiceItemTranslation translation) {
        translation.setServiceItem(this);
        translations.add(translation);
    }


}