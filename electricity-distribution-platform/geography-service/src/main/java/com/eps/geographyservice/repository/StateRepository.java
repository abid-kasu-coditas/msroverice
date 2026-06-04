package com.eps.geographyservice.repository;

import com.eps.geographyservice.model.StateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<StateMaster, Long> {
}
