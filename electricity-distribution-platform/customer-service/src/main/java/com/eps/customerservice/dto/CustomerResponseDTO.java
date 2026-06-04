package com.eps.customerservice.dto;

import com.eps.customerservice.model.CustomerStatus;
import java.time.LocalDate;

public class CustomerResponseDTO {

    private Long id;
    private Long globalCustomerId;
    private String accountNumber;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String tariffType;
    private Long cityId;
    private Long areaId;
    private Long crmId;
    private String city;
    private String district;
    private String state;
    private CustomerStatus status;
    private LocalDate registeredAt;
    private Boolean active;

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(Long id, String name, String email, String phone, String address,
                                String city, String district, String state, CustomerStatus status,
                                LocalDate registeredAt, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.district = district;
        this.state = state;
        this.status = status;
        this.registeredAt = registeredAt;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGlobalCustomerId() { return globalCustomerId; }
    public void setGlobalCustomerId(Long globalCustomerId) { this.globalCustomerId = globalCustomerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTariffType() { return tariffType; }
    public void setTariffType(String tariffType) { this.tariffType = tariffType; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public Long getCrmId() { return crmId; }
    public void setCrmId(Long crmId) { this.crmId = crmId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }

    public LocalDate getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDate registeredAt) { this.registeredAt = registeredAt; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
