package com.eps.customerservice.mapper;

import com.eps.customerservice.dto.CustomerRequestDTO;
import com.eps.customerservice.dto.CustomerResponseDTO;
import com.eps.customerservice.model.Customer;

public class CustomerMapper {

    public static Customer toModel(CustomerRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Customer(
            dto.getName(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getCity(),
            dto.getDistrict(),
            dto.getState()
        );
    }

    public static CustomerResponseDTO toDTO(Customer model) {
        if (model == null) {
            return null;
        }
        return new CustomerResponseDTO(
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
    }
}
