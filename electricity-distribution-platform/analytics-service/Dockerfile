FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar analytics-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "analytics-service.jar"]
