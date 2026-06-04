FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar meter-service.jar

EXPOSE 8082 9082

ENTRYPOINT ["java", "-jar", "meter-service.jar"]
