FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar connection-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "connection-service.jar"]
