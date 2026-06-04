package com.eps.complaintservice.repository;

import com.eps.complaintservice.model.ComplaintEscalation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintEscalationRepository extends JpaRepository<ComplaintEscalation, Long> {
}
