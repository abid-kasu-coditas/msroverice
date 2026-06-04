FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar client-onboarding-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "client-onboarding-service.jar"]
