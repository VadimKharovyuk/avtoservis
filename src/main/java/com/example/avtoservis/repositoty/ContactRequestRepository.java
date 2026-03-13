package com.example.avtoservis.repositoty;

import com.example.avtoservis.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.model.ContactRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    Page<ContactRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ContactRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status, Pageable pageable);

    long countByStatus(RequestStatus status);
}
