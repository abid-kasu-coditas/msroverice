FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar auth-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
