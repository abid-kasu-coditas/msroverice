package com.eps.customerservice.service;

import com.eps.customerservice.client.PaymentBlockServiceClient;
import com.eps.customerservice.dto.CustomerRequestDTO;
import com.eps.customerservice.dto.CustomerResponseDTO;
import com.eps.customerservice.exception.CustomerBlockedException;
import com.eps.customerservice.exception.CustomerNotFoundException;
import com.eps.customerservice.exception.EmailAlreadyExistsException;
import com.eps.customerservice.mapper.CustomerMapper;
import com.eps.customerservice.model.Customer;
import com.eps.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PaymentBlockServiceClient paymentBlockServiceClient;

    public CustomerService(CustomerRepository customerRepository,
                           PaymentBlockServiceClient paymentBlockServiceClient) {
        this.customerRepository = customerRepository;
        this.paymentBlockServiceClient = paymentBlockServiceClient;
    }

    public List<CustomerResponseDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerMapper::toDTO).toList();
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A customer with email " + customerRequestDTO.getEmail() + " already exists");
        }

        Customer newCustomer = customerRepository.save(CustomerMapper.toModel(customerRequestDTO));
        return CustomerMapper.toDTO(newCustomer);
    }

    public CustomerResponseDTO getCustomerById(UUID id) {
        ensureCustomerNotBlocked(id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return CustomerMapper.toDTO(customer);
    }

    public CustomerResponseDTO updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO) {
        ensureCustomerNotBlocked(id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        if (!customer.getEmail().equals(customerRequestDTO.getEmail()) && 
            customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A customer with email " + customerRequestDTO.getEmail() + " already exists");
        }

        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setPhone(customerRequestDTO.getPhone());
        customer.setAddress(customerRequestDTO.getAddress());
        customer.setCity(customerRequestDTO.getCity());
        customer.setDistrict(customerRequestDTO.getDistrict());
        customer.setState(customerRequestDTO.getState());

        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerMapper.toDTO(updatedCustomer);
    }

    public void deleteCustomer(UUID id) {
        ensureCustomerNotBlocked(id);
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    private void ensureCustomerNotBlocked(UUID customerId) {
        if (paymentBlockServiceClient.isCustomerBlocked(customerId)) {
            throw new CustomerBlockedException(
                "Customer " + customerId + " is blocked due to outstanding payments");
        }
    }
}
