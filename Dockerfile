
FROM eclipse-temurin:17-jdk-jammy AS ORCHASTRATORSERVICES

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src

EXPOSE 8080

LABEL authors="augustoribeiro"

CMD["./mvnw", "spring-boot:run"]