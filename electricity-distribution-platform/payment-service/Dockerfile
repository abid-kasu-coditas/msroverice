FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar payment-service.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "payment-service.jar"]
