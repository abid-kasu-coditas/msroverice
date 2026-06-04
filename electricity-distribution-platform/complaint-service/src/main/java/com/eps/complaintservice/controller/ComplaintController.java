package com.eps.complaintservice.controller;

import com.eps.complaintservice.dto.AssignTechnicianRequestDTO;
import com.eps.complaintservice.dto.ComplaintRequestDTO;
import com.eps.complaintservice.dto.ComplaintResponseDTO;
import com.eps.complaintservice.dto.ResolveComplaintRequestDTO;
import com.eps.complaintservice.model.ComplaintCategory;
import com.eps.complaintservice.model.ComplaintStatus;
import com.eps.complaintservice.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/complaints")
@Tag(name = "Complaint", description = "API for complaint tracking and resolution")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    @Operation(summary = "Get all complaints")
    public ResponseEntity<List<ComplaintResponseDTO>> getComplaints(
        @RequestParam(required = false) UUID customerId,
        @RequestParam(required = false) ComplaintStatus status,
        @RequestParam(required = false) ComplaintCategory category) {
        if (customerId != null) {
            return ResponseEntity.ok(complaintService.getComplaintsByCustomerId(customerId));
        }
        if (status != null) {
            return ResponseEntity.ok(complaintService.getComplaintsByStatus(status));
        }
        if (category != null) {
            return ResponseEntity.ok(complaintService.getComplaintsByCategory(category));
        }
        return ResponseEntity.ok(complaintService.getComplaints());
    }

    @PostMapping
    @Operation(summary = "Create a complaint")
    public ResponseEntity<ComplaintResponseDTO> createComplaint(@Valid @RequestBody ComplaintRequestDTO request) {
        return ResponseEntity.ok(complaintService.createComplaint(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get complaint by ID")
    public ResponseEntity<ComplaintResponseDTO> getComplaintById(@PathVariable UUID id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a complaint")
    public ResponseEntity<ComplaintResponseDTO> updateComplaint(@PathVariable UUID id,
        @Valid @RequestBody ComplaintRequestDTO request) {
        return ResponseEntity.ok(complaintService.updateComplaint(id, request));
    }

    @PatchMapping("/{id}/assign-technician")
    @Operation(summary = "Assign a technician to a complaint")
    public ResponseEntity<ComplaintResponseDTO> assignTechnician(@PathVariable UUID id,
        @Valid @RequestBody AssignTechnicianRequestDTO request,
        @RequestHeader(value = "X-Auth-User-Id", required = false) String assignedByUserId) {
        return ResponseEntity.ok(complaintService.assignTechnician(id, request.getTechnicianId(),
            parseUserId(assignedByUserId)));
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Resolve a complaint")
    public ResponseEntity<ComplaintResponseDTO> resolveComplaint(@PathVariable UUID id,
        @Valid @RequestBody ResolveComplaintRequestDTO request) {
        return ResponseEntity.ok(complaintService.resolveComplaint(id, request.getResolution()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a complaint")
    public ResponseEntity<Void> deleteComplaint(@PathVariable UUID id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }

    private UUID parseUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return UUID.fromString(userId);
    }
}
