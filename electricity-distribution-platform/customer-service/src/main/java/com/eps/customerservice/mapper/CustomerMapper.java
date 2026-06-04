package com.eps.customerservice.mapper;

import com.eps.customerservice.dto.CustomerRequestDTO;
import com.eps.customerservice.dto.CustomerResponseDTO;
import com.eps.customerservice.model.Customer;

public class CustomerMapper {

    public static Customer toModel(CustomerRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer(
            dto.getName(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getCity(),
            dto.getDistrict(),
            dto.getState()
        );
        customer.setGlobalCustomerId(dto.getGlobalCustomerId());
        customer.setAccountNumber(dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()
            ? "ACC-" + System.currentTimeMillis()
            : dto.getAccountNumber());
        customer.setTariffType(dto.getTariffType() == null || dto.getTariffType().isBlank()
            ? "DOMESTIC"
            : dto.getTariffType());
        customer.setCityId(dto.getCityId());
        customer.setAreaId(dto.getAreaId());
        customer.setCrmId(dto.getCrmId());
        return customer;
    }

    public static CustomerResponseDTO toDTO(Customer model) {
        if (model == null) {
            return null;
        }
        CustomerResponseDTO dto = new CustomerResponseDTO(
            model.getId(),
            model.getName(),
            model.getEmail(),
            model.getPhone(),
            model.getAddress(),
            model.getCity(),
            model.getDistrict(),
            model.getState(),
            model.getStatus(),
            model.getRegisteredAt(),
            model.getActive()
        );
        dto.setGlobalCustomerId(model.getGlobalCustomerId());
        dto.setAccountNumber(model.getAccountNumber());
        dto.setTariffType(model.getTariffType());
        dto.setCityId(model.getCityId());
        dto.setAreaId(model.getAreaId());
        dto.setCrmId(model.getCrmId());
        return dto;
    }
}
