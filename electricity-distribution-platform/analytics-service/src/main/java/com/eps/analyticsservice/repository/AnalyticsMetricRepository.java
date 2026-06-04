package com.eps.analyticsservice.repository;

import com.eps.analyticsservice.model.AnalyticsMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsMetricRepository extends JpaRepository<AnalyticsMetric, UUID> {

    Page<AnalyticsMetric> findByMetricType(AnalyticsMetric.MetricType metricType, Pageable pageable);

    Page<AnalyticsMetric> findByCustomerId(UUID customerId, Pageable pageable);

    List<AnalyticsMetric> findByMetricType(AnalyticsMetric.MetricType metricType);

    List<AnalyticsMetric> findByRecordedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<AnalyticsMetric> findAll(Pageable pageable);

    long countByMetricType(AnalyticsMetric.MetricType metricType);

    @Query("SELECT SUM(m.metricValue) FROM AnalyticsMetric m WHERE m.metricType = :metricType")
    Double sumByMetricType(AnalyticsMetric.MetricType metricType);

    @Query("SELECT AVG(m.metricValue) FROM AnalyticsMetric m WHERE m.metricType = :metricType")
    Double averageByMetricType(AnalyticsMetric.MetricType metricType);
}
