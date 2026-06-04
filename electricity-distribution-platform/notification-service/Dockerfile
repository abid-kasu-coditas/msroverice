FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar notification-service.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "notification-service.jar"]
