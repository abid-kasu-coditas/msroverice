package com.eps.geographyservice.grpc;

import com.eps.geographyservice.model.Area;
import com.eps.geographyservice.model.City;
import com.eps.geographyservice.service.GeographyLookupService;
import com.eps.grpc.common.IdRequest;
import com.eps.grpc.common.ValidateResponse;
import com.eps.grpc.geography.AreaResponse;
import com.eps.grpc.geography.CityResponse;
import com.eps.grpc.geography.GeographyServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GeographyGrpcService extends GeographyServiceGrpc.GeographyServiceImplBase {

  private final GeographyLookupService geographyLookupService;

  public GeographyGrpcService(GeographyLookupService geographyLookupService) {
    this.geographyLookupService = geographyLookupService;
  }

  @Override
  public void getAreaById(IdRequest request, StreamObserver<AreaResponse> responseObserver) {
    Area area = geographyLookupService.area(request.getId());
    responseObserver.onNext(AreaResponse.newBuilder()
        .setId(area.getId())
        .setCityId(area.getCityId())
        .setName(area.getName())
        .setPincode(area.getPincode() == null ? "" : area.getPincode())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void getCityById(IdRequest request, StreamObserver<CityResponse> responseObserver) {
    City city = geographyLookupService.city(request.getId());
    responseObserver.onNext(CityResponse.newBuilder()
        .setId(city.getId())
        .setDistrictId(city.getDistrictId())
        .setName(city.getName())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void validateArea(IdRequest request, StreamObserver<ValidateResponse> responseObserver) {
    boolean valid = geographyLookupService.areaExists(request.getId());
    responseObserver.onNext(ValidateResponse.newBuilder()
        .setValid(valid)
        .setMessage(valid ? "Area exists" : "Area not found")
        .build());
    responseObserver.onCompleted();
  }
}
