FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar complaint-service.jar

EXPOSE 8092

ENTRYPOINT ["java", "-jar", "complaint-service.jar"]
