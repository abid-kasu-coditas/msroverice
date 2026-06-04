package com.eps.connectionservice.repository;

import com.eps.connectionservice.model.Connection;
import com.eps.connectionservice.model.ConnectionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, UUID> {

    Optional<Connection> findByConnectionNumber(String connectionNumber);

    boolean existsByConnectionNumber(String connectionNumber);

    boolean existsByConnectionNumberAndIdNot(String connectionNumber, UUID id);

    List<Connection> findByCustomerId(UUID customerId);

    List<Connection> findByStatus(ConnectionStatus status);
}
