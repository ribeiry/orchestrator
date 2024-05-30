
FROM openjdk:22-ea-17-jdk-slim AS ORCHASTRATORSERVICES

WORKDIR /app

# Set the JVM memory options for the Spring Boot application
ENV JAVA_OPTS = "-Xmx1G -Xms512m -XX:+UseCGroupMemoryLimitForHeap+UseG1GC"

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src

EXPOSE 8080

LABEL authors="augustoribeiro"

CMD ["./mvnw", "spring-boot:run"]