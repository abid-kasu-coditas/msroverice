package com.eps.apigateway;

import com.eps.apigateway.filter.JwtValidationGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class ApiGatewayApplication {

    private static final String AUTH_SERVICE = "http://auth-service:8080";
    private static final String CUSTOMER_SERVICE = "http://customer-service:8081";
    private static final String METER_SERVICE = "http://meter-service:8082";
    private static final String CONNECTION_SERVICE = "http://connection-service:8095";
    private static final String ANALYTICS_SERVICE = "http://analytics-service:8083";
    private static final String PLATFORM_SERVICE = "http://platform-service:8085";
    private static final String TENANT_PROVISIONING_SERVICE = "http://tenant-provisioning-service:8086";
    private static final String GEOGRAPHY_SERVICE = "http://geography-service:8087";
    private static final String TENANT_USER_SERVICE = "http://tenant-user-service:8088";
    private static final String METER_READING_SERVICE = "http://meter-reading-service:8089";
    private static final String BILLING_SERVICE = "http://billing-service:8090";
    private static final String PLATFORM_BILLING_SERVICE = "http://platform-billing-service:8091";
    private static final String COMPLAINT_SERVICE = "http://complaint-service:8092";
    private static final String PAYMENT_SERVICE = "http://payment-service:8093";
    private static final String NOTIFICATION_SERVICE = "http://notification-service:8094";

    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String MANAGEMENT = "MANAGEMENT";
    private static final String SALES_POC = "SALES_POC";
    private static final String STATE_HEAD = "STATE_HEAD";
    private static final String DISTRICT_HEAD = "DISTRICT_HEAD";
    private static final String CITY_HEAD = "CITY_HEAD";
    private static final String CRM = "CRM";
    private static final String TECHNICIAN = "TECHNICIAN";
    private static final String BILLER = "BILLER";
    private static final String OPERATIONS = "OPERATIONS";
    private static final String BPO_EMPLOYEE = "BPO_EMPLOYEE";
    private static final String BPO_MANAGER_L1 = "BPO_MANAGER_L1";
    private static final String BPO_MANAGER_L2 = "BPO_MANAGER_L2";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String CLIENT_OPERATIONS = "CLIENT_OPERATIONS";
    private static final String CLIENT_BPO_EMPLOYEE = "CLIENT_BPO_EMPLOYEE";
    private static final String CLIENT_BPO_MANAGER_L1 = "CLIENT_BPO_MANAGER_L1";
    private static final String CLIENT_BPO_MANAGER_L2 = "CLIENT_BPO_MANAGER_L2";
    private static final String CLIENT_SALES_POC = "CLIENT_SALES_POC";

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,
            JwtValidationGatewayFilterFactory jwtValidation) {
        return builder.routes()
            .route("auth-public", r -> r
                .path("/auth/login", "/auth/validate")
                .uri(AUTH_SERVICE))
            .route("auth-refresh", r -> r
                .path("/auth/refresh-token")
                .filters(f -> f.filter(jwtValidation.apply(allPlatformAndClientRoles())))
                .uri(AUTH_SERVICE))
            .route("auth-management", r -> r
                .path("/auth/register", "/auth/users", "/auth/users/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(AUTH_SERVICE))
            .route("employee-delete", r -> r
                .path("/api/tenant-users", "/api/tenant-users/**")
                .and()
                .method(HttpMethod.DELETE)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN))))
                .uri(TENANT_USER_SERVICE))
            .route("employee-write", r -> r
                .path("/api/tenant-users", "/api/tenant-users/**")
                .and()
                .method(HttpMethod.POST, HttpMethod.PUT)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, OPERATIONS))))
                .uri(TENANT_USER_SERVICE))
            .route("employee-read", r -> r
                .path("/api/tenant-users", "/api/tenant-users/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, OPERATIONS, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD))))
                .uri(TENANT_USER_SERVICE))
            .route("onboarding-delete", r -> r
                .path("/api/platform/tenants", "/api/platform/tenants/**")
                .and()
                .method(HttpMethod.DELETE)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(PLATFORM_SERVICE))
            .route("onboarding-write", r -> r
                .path("/api/platform/tenants", "/api/platform/tenants/**")
                .and()
                .method(HttpMethod.POST)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, SALES_POC))))
                .uri(PLATFORM_SERVICE))
            .route("onboarding-read", r -> r
                .path("/api/platform/tenants", "/api/platform/tenants/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, SALES_POC))))
                .uri(PLATFORM_SERVICE))
            .route("tenant-provisioning-service", r -> r
                .path("/api/provisioning", "/api/provisioning/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(TENANT_PROVISIONING_SERVICE))
            .route("geography-service", r -> r
                .path("/api/geography", "/api/geography/**")
                .filters(f -> f.filter(jwtValidation.apply(allPlatformAndTenantRoles())))
                .uri(GEOGRAPHY_SERVICE))
            .route("customer-service", r -> r
                .path("/api/customers", "/api/customers/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD))))
                .uri(CUSTOMER_SERVICE))
            .route("connection-service", r -> r
                .path("/api/connections", "/api/connections/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD))))
                .uri(CONNECTION_SERVICE))
            .route("meter-service", r -> r
                .path("/api/meters", "/api/meters/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, TECHNICIAN, BILLER, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD))))
                .uri(METER_SERVICE))
            .route("meter-reading-service", r -> r
                .path("/api/readings", "/api/readings/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, BILLER, TECHNICIAN))))
                .uri(METER_READING_SERVICE))
            .route("billing-service", r -> r
                .path("/api/bills", "/api/bills/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, BILLER, CUSTOMER))))
                .uri(BILLING_SERVICE))
            .route("payment-service", r -> r
                .path("/api/payments", "/api/payments/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, BILLER, CUSTOMER))))
                .uri(PAYMENT_SERVICE))
            .route("platform-billing-service", r -> r
                .path("/api/platform-billing", "/api/platform-billing/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(PLATFORM_BILLING_SERVICE))
            .route("complaint-delete", r -> r
                .path("/api/complaints", "/api/complaints/**")
                .and()
                .method(HttpMethod.DELETE)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-assign-technician", r -> r
                .path("/api/complaints/*/assign-technician")
                .and()
                .method(HttpMethod.PATCH)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, CITY_HEAD, CLIENT_BPO_EMPLOYEE,
                    CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2, BPO_EMPLOYEE,
                    BPO_MANAGER_L1, BPO_MANAGER_L2))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-resolve", r -> r
                .path("/api/complaints/*/resolve")
                .and()
                .method(HttpMethod.PATCH)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD, CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2,
                    BPO_MANAGER_L1, BPO_MANAGER_L2))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-write", r -> r
                .path("/api/complaints", "/api/complaints/**")
                .and()
                .method(HttpMethod.POST, HttpMethod.PUT)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD,
                    CLIENT_BPO_EMPLOYEE, CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2,
                    BPO_EMPLOYEE, BPO_MANAGER_L1, BPO_MANAGER_L2, CUSTOMER))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-read", r -> r
                .path("/api/complaints", "/api/complaints/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD, CLIENT_BPO_EMPLOYEE, CLIENT_BPO_MANAGER_L1,
                    CLIENT_BPO_MANAGER_L2, BPO_EMPLOYEE, BPO_MANAGER_L1,
                    BPO_MANAGER_L2, CUSTOMER))))
                .uri(COMPLAINT_SERVICE))
            .route("notification-service", r -> r
                .path("/api/notifications", "/api/notifications/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, CRM))))
                .uri(NOTIFICATION_SERVICE))
            .route("analytics-service", r -> r
                .path("/api/analytics", "/api/analytics/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD))))
                .uri(ANALYTICS_SERVICE))
            .route("audit-service", r -> r
                .path("/api/audit", "/api/audit/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(ANALYTICS_SERVICE))
            .build();
    }

    private JwtValidationGatewayFilterFactory.Config roles(String... roles) {
        return JwtValidationGatewayFilterFactory.withRoles(roles);
    }

    private JwtValidationGatewayFilterFactory.Config allPlatformAndClientRoles() {
        return roles(SUPER_ADMIN, MANAGEMENT, SALES_POC, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD,
            CRM, TECHNICIAN, BILLER, CLIENT_OPERATIONS, CLIENT_BPO_EMPLOYEE,
            CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2, CLIENT_SALES_POC,
            OPERATIONS, BPO_EMPLOYEE, BPO_MANAGER_L1, BPO_MANAGER_L2, CUSTOMER);
    }

    private JwtValidationGatewayFilterFactory.Config allPlatformAndTenantRoles() {
        return roles(SUPER_ADMIN, MANAGEMENT, SALES_POC, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD,
            CRM, TECHNICIAN, BILLER, OPERATIONS, BPO_EMPLOYEE, BPO_MANAGER_L1,
            BPO_MANAGER_L2, CUSTOMER);
    }
}
