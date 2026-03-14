package com.example.avtoservis.repositoty;

import com.example.avtoservis.model.ServiceItem;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    long countByActiveTrue();

    @Query("SELECT COALESCE(SUM(s.views), 0) FROM ServiceItem s")
    long sumViews();

    List<ServiceItem> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);


    List<ServiceItem> findByActiveTrueOrderByCreatedAtDesc();
}
