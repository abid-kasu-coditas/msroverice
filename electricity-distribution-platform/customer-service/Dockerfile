FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar customer-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "customer-service.jar"]
