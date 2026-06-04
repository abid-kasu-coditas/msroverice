FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar audit-service.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "audit-service.jar"]
