package com.eps.tenantuserservice.grpc;

import com.eps.grpc.common.IdRequest;
import com.eps.grpc.tenantuser.BPOEmployeeResponse;
import com.eps.grpc.tenantuser.ManagerRequest;
import com.eps.grpc.tenantuser.ManagerResponse;
import com.eps.grpc.tenantuser.TechnicianResponse;
import com.eps.grpc.tenantuser.TenantUserServiceGrpc;
import com.eps.shared.tenant.TenantContext;
import com.eps.tenantuserservice.model.TenantUser;
import com.eps.tenantuserservice.service.TenantUserManagementService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TenantUserGrpcService extends TenantUserServiceGrpc.TenantUserServiceImplBase {

  private final TenantUserManagementService tenantUserManagementService;

  public TenantUserGrpcService(TenantUserManagementService tenantUserManagementService) {
    this.tenantUserManagementService = tenantUserManagementService;
  }

  @Override
  public void getTechnicianByArea(IdRequest request,
      StreamObserver<TechnicianResponse> responseObserver) {
    withTenant(request.getTenantId(), () -> {
      TenantUser user = tenantUserManagementService.technicianByArea(request.getId());
      responseObserver.onNext(TechnicianResponse.newBuilder()
          .setId(user.getId())
          .setName(user.getName())
          .setAreaId(user.getAreaId() == null ? 0 : user.getAreaId())
          .setPhone(user.getPhone() == null ? "" : user.getPhone())
          .build());
      responseObserver.onCompleted();
    });
  }

  @Override
  public void getBPOEmployeeByCity(IdRequest request,
      StreamObserver<BPOEmployeeResponse> responseObserver) {
    withTenant(request.getTenantId(), () -> {
      TenantUser user = tenantUserManagementService.bpoByCity(request.getId());
      responseObserver.onNext(BPOEmployeeResponse.newBuilder()
          .setId(user.getId())
          .setName(user.getName())
          .setCityId(user.getCityId() == null ? 0 : user.getCityId())
          .setPhone(user.getPhone() == null ? "" : user.getPhone())
          .build());
      responseObserver.onCompleted();
    });
  }

  @Override
  public void getManagerByLevel(ManagerRequest request,
      StreamObserver<ManagerResponse> responseObserver) {
    withTenant(request.getTenantId(), () -> {
      TenantUser user = tenantUserManagementService.managerByLevel(request.getLevel(), request.getCityId());
      responseObserver.onNext(ManagerResponse.newBuilder()
          .setId(user.getId())
          .setName(user.getName())
          .setLevel(request.getLevel())
          .setPhone(user.getPhone() == null ? "" : user.getPhone())
          .build());
      responseObserver.onCompleted();
    });
  }

  private void withTenant(String tenantId, Runnable work) {
    if (tenantId != null && !tenantId.isBlank()) {
      TenantContext.setCurrentTenant(tenantId);
    }
    try {
      work.run();
    } finally {
      TenantContext.clear();
    }
  }
}
