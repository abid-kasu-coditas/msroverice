FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar api-gateway.jar

EXPOSE 4004

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
