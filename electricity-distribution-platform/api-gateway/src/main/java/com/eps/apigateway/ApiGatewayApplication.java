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

    private static final String AUTH_SERVICE = "http://auth-service:8081";
    private static final String CUSTOMER_SERVICE = "http://customer-service:8082";
    private static final String CONNECTION_SERVICE = "http://connection-service:8083";
    private static final String METER_SERVICE = "http://meter-service:8084";
    private static final String BILLING_SERVICE = "http://billing-service:8085";
    private static final String PAYMENT_SERVICE = "http://payment-service:8086";
    private static final String COMPLAINT_SERVICE = "http://complaint-service:8087";
    private static final String NOTIFICATION_SERVICE = "http://notification-service:8088";
    private static final String AUDIT_SERVICE = "http://audit-service:8089";
    private static final String CLIENT_ONBOARDING_SERVICE = "http://client-onboarding-service:8090";
    private static final String EMPLOYEE_SERVICE = "http://employee-service:8091";
    private static final String ANALYTICS_SERVICE = "http://analytics-service:8092";

    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private static final String MANAGEMENT = "MANAGEMENT";
    private static final String SALES_POC = "SALES_POC";
    private static final String STATE_HEAD = "STATE_HEAD";
    private static final String DISTRICT_HEAD = "DISTRICT_HEAD";
    private static final String CITY_HEAD = "CITY_HEAD";
    private static final String CRM = "CRM";
    private static final String TECHNICIAN = "TECHNICIAN";
    private static final String BILLER = "BILLER";
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
                .path("/api/employees", "/api/employees/**")
                .and()
                .method(HttpMethod.DELETE)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN))))
                .uri(EMPLOYEE_SERVICE))
            .route("employee-write", r -> r
                .path("/api/employees", "/api/employees/**")
                .and()
                .method(HttpMethod.POST, HttpMethod.PUT)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(EMPLOYEE_SERVICE))
            .route("employee-read", r -> r
                .path("/api/employees", "/api/employees/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD))))
                .uri(EMPLOYEE_SERVICE))
            .route("onboarding-delete", r -> r
                .path("/api/onboarding", "/api/onboarding/**")
                .and()
                .method(HttpMethod.DELETE)
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT))))
                .uri(CLIENT_ONBOARDING_SERVICE))
            .route("onboarding-write", r -> r
                .path("/api/onboarding", "/api/onboarding/**")
                .and()
                .method(HttpMethod.POST)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, SALES_POC))))
                .uri(CLIENT_ONBOARDING_SERVICE))
            .route("onboarding-read", r -> r
                .path("/api/onboarding", "/api/onboarding/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, SALES_POC))))
                .uri(CLIENT_ONBOARDING_SERVICE))
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
            .route("billing-service", r -> r
                .path("/api/bills", "/api/bills/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, BILLER))))
                .uri(BILLING_SERVICE))
            .route("payment-service", r -> r
                .path("/api/payments", "/api/payments/**")
                .filters(f -> f.filter(jwtValidation.apply(roles(SUPER_ADMIN, MANAGEMENT, BILLER))))
                .uri(PAYMENT_SERVICE))
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
                    CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-resolve", r -> r
                .path("/api/complaints/*/resolve")
                .and()
                .method(HttpMethod.PATCH)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD, CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-write", r -> r
                .path("/api/complaints", "/api/complaints/**")
                .and()
                .method(HttpMethod.POST, HttpMethod.PUT)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD,
                    CLIENT_BPO_EMPLOYEE, CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2))))
                .uri(COMPLAINT_SERVICE))
            .route("complaint-read", r -> r
                .path("/api/complaints", "/api/complaints/**")
                .and()
                .method(HttpMethod.GET)
                .filters(f -> f.filter(jwtValidation.apply(roles(
                    SUPER_ADMIN, MANAGEMENT, CRM, TECHNICIAN, STATE_HEAD, DISTRICT_HEAD,
                    CITY_HEAD, CLIENT_BPO_EMPLOYEE, CLIENT_BPO_MANAGER_L1,
                    CLIENT_BPO_MANAGER_L2))))
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
                .uri(AUDIT_SERVICE))
            .build();
    }

    private JwtValidationGatewayFilterFactory.Config roles(String... roles) {
        return JwtValidationGatewayFilterFactory.withRoles(roles);
    }

    private JwtValidationGatewayFilterFactory.Config allPlatformAndClientRoles() {
        return roles(SUPER_ADMIN, MANAGEMENT, SALES_POC, STATE_HEAD, DISTRICT_HEAD, CITY_HEAD,
            CRM, TECHNICIAN, BILLER, CLIENT_OPERATIONS, CLIENT_BPO_EMPLOYEE,
            CLIENT_BPO_MANAGER_L1, CLIENT_BPO_MANAGER_L2, CLIENT_SALES_POC);
    }
}
