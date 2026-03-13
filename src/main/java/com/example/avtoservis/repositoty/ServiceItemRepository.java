package com.example.avtoservis.repositoty;

import com.example.avtoservis.model.ServiceItem;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    // ServiceItemRepository
    long countByActiveTrue();

    @Query("SELECT COALESCE(SUM(s.views), 0) FROM ServiceItem s")
    long sumViews();
}
