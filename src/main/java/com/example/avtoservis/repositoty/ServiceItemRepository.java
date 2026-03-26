package com.example.avtoservis.repositoty;

import com.example.avtoservis.model.ServiceItem;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    long countByActiveTrue();

    @Query("SELECT COALESCE(SUM(s.views), 0) FROM ServiceItem s")
    long sumViews();

    List<ServiceItem> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);


    List<ServiceItem> findByActiveTrueOrderByCreatedAtDesc();


    @Query("SELECT s FROM ServiceItem s LEFT JOIN FETCH s.translations")
    Page<ServiceItem> findAllWithTranslations(Pageable pageable);


    @Modifying
    @Query("UPDATE ServiceItem s SET s.views = s.views + 1 WHERE s.id = :id")
    void incrementViews(@Param("id") Long id);

}
