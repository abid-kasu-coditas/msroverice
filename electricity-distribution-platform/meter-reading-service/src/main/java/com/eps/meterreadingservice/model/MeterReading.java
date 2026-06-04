package com.eps.meterreadingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings")
public class MeterReading {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long connectionId;

  private Long billerId;

  @Column(nullable = false)
  private BigDecimal readingValue;

  private BigDecimal previousReadingValue;

  @Column(nullable = false)
  private LocalDateTime readAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime recordedAt;

  @PrePersist
  void prePersist() {
    if (readAt == null) {
      readAt = LocalDateTime.now();
    }
    recordedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(Long connectionId) {
    this.connectionId = connectionId;
  }

  public Long getBillerId() {
    return billerId;
  }

  public void setBillerId(Long billerId) {
    this.billerId = billerId;
  }

  public BigDecimal getReadingValue() {
    return readingValue;
  }

  public void setReadingValue(BigDecimal readingValue) {
    this.readingValue = readingValue;
  }

  public BigDecimal getPreviousReadingValue() {
    return previousReadingValue;
  }

  public void setPreviousReadingValue(BigDecimal previousReadingValue) {
    this.previousReadingValue = previousReadingValue;
  }

  public LocalDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(LocalDateTime readAt) {
    this.readAt = readAt;
  }
}
