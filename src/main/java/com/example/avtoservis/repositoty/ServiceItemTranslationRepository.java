package com.example.avtoservis.repositoty;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.model.ServiceItemTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceItemTranslationRepository extends JpaRepository<ServiceItemTranslation, Long> {

    Optional<ServiceItemTranslation> findBySlugAndLanguage(String slug, Language language);

    boolean existsBySlugAndLanguage(String slug, Language language);
}
