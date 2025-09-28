FROM maven:3.9.9-eclipse-temurin-23-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY --from=build /app/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]