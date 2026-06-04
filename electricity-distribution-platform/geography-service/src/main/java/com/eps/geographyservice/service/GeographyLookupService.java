package com.eps.geographyservice.service;

import com.eps.geographyservice.model.Area;
import com.eps.geographyservice.model.City;
import com.eps.geographyservice.model.District;
import com.eps.geographyservice.model.StateMaster;
import com.eps.geographyservice.repository.AreaRepository;
import com.eps.geographyservice.repository.CityRepository;
import com.eps.geographyservice.repository.DistrictRepository;
import com.eps.geographyservice.repository.StateRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GeographyLookupService {

  private final StateRepository stateRepository;
  private final DistrictRepository districtRepository;
  private final CityRepository cityRepository;
  private final AreaRepository areaRepository;

  public GeographyLookupService(StateRepository stateRepository,
      DistrictRepository districtRepository, CityRepository cityRepository,
      AreaRepository areaRepository) {
    this.stateRepository = stateRepository;
    this.districtRepository = districtRepository;
    this.cityRepository = cityRepository;
    this.areaRepository = areaRepository;
  }

  public List<StateMaster> states() {
    return stateRepository.findAll();
  }

  public List<District> districts(Long stateId) {
    return districtRepository.findByStateId(stateId);
  }

  public List<City> cities(Long districtId) {
    return cityRepository.findByDistrictId(districtId);
  }

  public List<Area> areas(Long cityId) {
    return areaRepository.findByCityId(cityId);
  }

  public City city(Long id) {
    return cityRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("City not found: " + id));
  }

  public Area area(Long id) {
    return areaRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Area not found: " + id));
  }

  public boolean areaExists(Long id) {
    return areaRepository.existsById(id);
  }
}
