FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar meter-service.jar

EXPOSE 8084 9084

ENTRYPOINT ["java", "-jar", "meter-service.jar"]
