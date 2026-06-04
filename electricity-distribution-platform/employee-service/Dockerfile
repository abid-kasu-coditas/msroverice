FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar employee-service.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "employee-service.jar"]
