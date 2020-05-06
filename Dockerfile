FROM openjdk:11.0.7-jdk-slim as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM openjdk:11.0.7-jre-slim
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar .
ENTRYPOINT ["java","-jar","spring-event-notify-demo.jar"]
