FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar billing-service.jar

EXPOSE 8090 9090

ENTRYPOINT ["java", "-jar", "billing-service.jar"]
