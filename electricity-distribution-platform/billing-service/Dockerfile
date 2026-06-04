FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar billing-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "billing-service.jar"]
