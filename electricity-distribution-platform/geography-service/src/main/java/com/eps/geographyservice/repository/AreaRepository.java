package com.eps.geographyservice.repository;

import com.eps.geographyservice.model.Area;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

  List<Area> findByCityId(Long cityId);
}
