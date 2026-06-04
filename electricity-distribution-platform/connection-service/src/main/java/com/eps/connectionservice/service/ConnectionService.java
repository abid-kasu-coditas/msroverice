package com.eps.connectionservice.service;

import com.eps.connectionservice.client.PaymentBlockServiceClient;
import com.eps.connectionservice.dto.ConnectionRequestDTO;
import com.eps.connectionservice.dto.ConnectionResponseDTO;
import com.eps.connectionservice.exception.CustomerBlockedException;
import com.eps.connectionservice.exception.ConnectionNotFoundException;
import com.eps.connectionservice.exception.ConnectionNumberAlreadyExistsException;
import com.eps.connectionservice.mapper.ConnectionMapper;
import com.eps.connectionservice.model.Connection;
import com.eps.connectionservice.model.ConnectionStatus;
import com.eps.connectionservice.repository.ConnectionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final PaymentBlockServiceClient paymentBlockServiceClient;

    public ConnectionService(ConnectionRepository connectionRepository,
                             PaymentBlockServiceClient paymentBlockServiceClient) {
        this.connectionRepository = connectionRepository;
        this.paymentBlockServiceClient = paymentBlockServiceClient;
    }

    public List<ConnectionResponseDTO> getConnections() {
        return connectionRepository.findAll().stream().map(ConnectionMapper::toDTO).toList();
    }

    public List<ConnectionResponseDTO> getConnectionsByCustomerId(UUID customerId) {
        return connectionRepository.findByCustomerId(customerId).stream()
            .map(ConnectionMapper::toDTO)
            .toList();
    }

    public List<ConnectionResponseDTO> getConnectionsByStatus(ConnectionStatus status) {
        return connectionRepository.findByStatus(status).stream().map(ConnectionMapper::toDTO).toList();
    }

    public ConnectionResponseDTO createConnection(ConnectionRequestDTO request) {
        ensureCustomerNotBlocked(request.getCustomerId());
        if (connectionRepository.existsByConnectionNumber(request.getConnectionNumber())) {
            throw new ConnectionNumberAlreadyExistsException(
                "A connection with number " + request.getConnectionNumber() + " already exists");
        }

        Connection newConnection = connectionRepository.save(ConnectionMapper.toModel(request));
        return ConnectionMapper.toDTO(newConnection);
    }

    public ConnectionResponseDTO getConnectionById(UUID id) {
        Connection connection = findConnection(id);
        return ConnectionMapper.toDTO(connection);
    }

    public ConnectionResponseDTO updateConnection(UUID id, ConnectionRequestDTO request) {
        Connection connection = findConnection(id);
        ensureCustomerNotBlocked(request.getCustomerId());

        if (connectionRepository.existsByConnectionNumberAndIdNot(request.getConnectionNumber(), id)) {
            throw new ConnectionNumberAlreadyExistsException(
                "A connection with number " + request.getConnectionNumber() + " already exists");
        }

        connection.setCustomerId(request.getCustomerId());
        connection.setConnectionNumber(request.getConnectionNumber());
        connection.setServiceAddress(request.getServiceAddress());
        connection.setTariffPlan(request.getTariffPlan());
        connection.setLoadCapacity(request.getLoadCapacity());
        connection.setStatus(request.getStatus() == null ? connection.getStatus() : request.getStatus());
        connection.setConnectionDate(request.getConnectionDate() == null ? LocalDate.now() : request.getConnectionDate());
        connection.setTerminationDate(request.getTerminationDate());

        return ConnectionMapper.toDTO(connectionRepository.save(connection));
    }

    public void deleteConnection(UUID id) {
        if (!connectionRepository.existsById(id)) {
            throw new ConnectionNotFoundException("Connection not found with ID: " + id);
        }
        connectionRepository.deleteById(id);
    }

    private Connection findConnection(UUID id) {
        return connectionRepository.findById(id)
            .orElseThrow(() -> new ConnectionNotFoundException("Connection not found with ID: " + id));
    }

    private void ensureCustomerNotBlocked(UUID customerId) {
        if (paymentBlockServiceClient.isCustomerBlocked(customerId)) {
            throw new CustomerBlockedException(
                "Customer " + customerId + " is blocked due to outstanding payments");
        }
    }
}
