package com.example.avtoservis.model;
import com.example.avtoservis.enums.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "service_item_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"service_item_id", "language"}))
public class ServiceItemTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_item_id", nullable = false)
    private ServiceItem serviceItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    // --- Контент ---
    private String name;

    @Column(length = 2000)
    private String description;

    // --- SEO (кожна мова — свій slug для індексації) ---
    @Column(unique = true)
    private String slug;

    @Column(length = 160)
    private String metaTitle;

    @Column(length = 300)
    private String metaDescription;

    @Column(length = 500)
    private String imageAlt;
}
