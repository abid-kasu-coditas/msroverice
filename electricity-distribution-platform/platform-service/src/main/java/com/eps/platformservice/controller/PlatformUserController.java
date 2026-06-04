package com.eps.platformservice.controller;

import com.eps.platformservice.dto.PlatformUserRequest;
import com.eps.platformservice.dto.PocTenantAssignmentRequest;
import com.eps.platformservice.model.PlatformUser;
import com.eps.platformservice.model.PocTenantAssignment;
import com.eps.platformservice.service.PlatformUserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platform")
public class PlatformUserController {

  private final PlatformUserService platformUserService;

  public PlatformUserController(PlatformUserService platformUserService) {
    this.platformUserService = platformUserService;
  }

  @PostMapping("/users")
  public ResponseEntity<PlatformUser> createUser(@Valid @RequestBody PlatformUserRequest request) {
    return ResponseEntity.ok(platformUserService.createUser(request));
  }

  @GetMapping("/users")
  public ResponseEntity<List<PlatformUser>> findUsers() {
    return ResponseEntity.ok(platformUserService.findAllUsers());
  }

  @PostMapping("/poc-assignments")
  public ResponseEntity<PocTenantAssignment> assignPoc(
      @Valid @RequestBody PocTenantAssignmentRequest request) {
    return ResponseEntity.ok(platformUserService.assignPoc(request));
  }

  @GetMapping("/poc-assignments")
  public ResponseEntity<List<PocTenantAssignment>> findAssignments() {
    return ResponseEntity.ok(platformUserService.findAllAssignments());
  }
}
