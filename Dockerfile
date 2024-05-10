# Use the official Gradle image to build the application
FROM gradle:7.5.0-jdk17 as build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle clean build -x test --no-daemon

# Use OpenJDK 17 to run the application
FROM openjdk:17-slim
EXPOSE 8080
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
