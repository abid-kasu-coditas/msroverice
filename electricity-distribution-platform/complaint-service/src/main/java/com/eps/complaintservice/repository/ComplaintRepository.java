package com.eps.complaintservice.repository;

import com.eps.complaintservice.model.Complaint;
import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    List<Complaint> findByCustomerId(UUID customerId);

    List<Complaint> findByStatus(ComplaintStatus status);

    List<Complaint> findByCategory(ComplaintCategory category);
}
