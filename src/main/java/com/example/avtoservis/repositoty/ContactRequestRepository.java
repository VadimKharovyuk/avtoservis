package com.example.avtoservis.repositoty;

import com.example.avtoservis.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
}
