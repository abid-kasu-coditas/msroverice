package com.eps.analyticsservice.mapper;

import com.eps.analyticsservice.dto.AnalyticsMetricDTO;
import com.eps.analyticsservice.model.AnalyticsMetric;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsMetricMapper {

    public AnalyticsMetricDTO toDTO(AnalyticsMetric metric) {
        if (metric == null) {
            return null;
        }

        AnalyticsMetricDTO dto = new AnalyticsMetricDTO();
        dto.setId(metric.getId());
        dto.setMetricType(metric.getMetricType() != null ? metric.getMetricType().name() : null);
        dto.setCustomerId(metric.getCustomerId());
        dto.setConnectionId(metric.getConnectionId());
        dto.setBillId(metric.getBillId());
        dto.setPaymentId(metric.getPaymentId());
        dto.setMetricValue(metric.getMetricValue());
        dto.setMetricUnit(metric.getMetricUnit());
        dto.setDescription(metric.getDescription());
        dto.setRecordedAt(metric.getRecordedAt());
        dto.setCreatedAt(metric.getCreatedAt());

        return dto;
    }

    public AnalyticsMetric toModel(AnalyticsMetricDTO dto) {
        if (dto == null) {
            return null;
        }

        AnalyticsMetric metric = new AnalyticsMetric();
        metric.setId(dto.getId());
        if (dto.getMetricType() != null) {
            metric.setMetricType(AnalyticsMetric.MetricType.valueOf(dto.getMetricType()));
        }
        metric.setCustomerId(dto.getCustomerId());
        metric.setConnectionId(dto.getConnectionId());
        metric.setBillId(dto.getBillId());
        metric.setPaymentId(dto.getPaymentId());
        metric.setMetricValue(dto.getMetricValue());
        metric.setMetricUnit(dto.getMetricUnit());
        metric.setDescription(dto.getDescription());
        metric.setRecordedAt(dto.getRecordedAt());
        metric.setCreatedAt(dto.getCreatedAt());

        return metric;
    }
}
