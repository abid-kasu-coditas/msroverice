package com.eps.meterreadingservice.controller;

import com.eps.meterreadingservice.dto.MeterReadingResponse;
import com.eps.meterreadingservice.dto.SubmitMeterReadingRequest;
import com.eps.meterreadingservice.model.MeterReading;
import com.eps.meterreadingservice.service.MeterReadingSubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readings")
public class MeterReadingController {

  private final MeterReadingSubmissionService meterReadingSubmissionService;

  public MeterReadingController(MeterReadingSubmissionService meterReadingSubmissionService) {
    this.meterReadingSubmissionService = meterReadingSubmissionService;
  }

  @PostMapping
  public ResponseEntity<MeterReadingResponse> submit(
      @Valid @RequestBody SubmitMeterReadingRequest request) {
    return ResponseEntity.ok(meterReadingSubmissionService.submit(request));
  }

  @GetMapping("/connections/{connectionId}")
  public ResponseEntity<List<MeterReading>> byConnection(@PathVariable Long connectionId) {
    return ResponseEntity.ok(meterReadingSubmissionService.findByConnection(connectionId));
  }
}
