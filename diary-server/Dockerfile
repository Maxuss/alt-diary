FROM openjdk:17-jdk-slim
VOLUME /main-app
ARG version
ADD build/libs/*-${version}-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app.jar"]