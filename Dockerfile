FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
ADD target/*.jar myapp.jar
ENTRYPOINT ["java", "-jar", "/myapp.jar"]
