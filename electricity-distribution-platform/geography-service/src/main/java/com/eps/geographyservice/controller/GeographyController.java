package com.eps.geographyservice.controller;

import com.eps.geographyservice.model.Area;
import com.eps.geographyservice.model.City;
import com.eps.geographyservice.model.District;
import com.eps.geographyservice.model.StateMaster;
import com.eps.geographyservice.service.GeographyLookupService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geography")
public class GeographyController {

  private final GeographyLookupService geographyLookupService;

  public GeographyController(GeographyLookupService geographyLookupService) {
    this.geographyLookupService = geographyLookupService;
  }

  @GetMapping("/states")
  public ResponseEntity<List<StateMaster>> states() {
    return ResponseEntity.ok(geographyLookupService.states());
  }

  @GetMapping("/states/{stateId}/districts")
  public ResponseEntity<List<District>> districts(@PathVariable Long stateId) {
    return ResponseEntity.ok(geographyLookupService.districts(stateId));
  }

  @GetMapping("/districts/{districtId}/cities")
  public ResponseEntity<List<City>> cities(@PathVariable Long districtId) {
    return ResponseEntity.ok(geographyLookupService.cities(districtId));
  }

  @GetMapping("/cities/{cityId}/areas")
  public ResponseEntity<List<Area>> areas(@PathVariable Long cityId) {
    return ResponseEntity.ok(geographyLookupService.areas(cityId));
  }
}
