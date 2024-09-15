# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS ORCHESTRATORSERVICE

# Diretório de trabalho no contêiner
WORKDIR /app

# Set the JVM memory options for the Spring Boot application
ENV JAVA_OPTS = "-Xmx1G -Xms512m -XX:+UseCGroupMemoryLimitForHeap+UseG1GC"

# Copie o arquivo JAR do seu aplicativo para o contêiner
COPY .mvn/ .mvn

COPY /app/mvnw  ./

COPY /app/pom.xml ./

# Atualize o gerenciador de pacotes e instale dependências necessárias
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    unzip \
    python3 \
    python3-pip \
    groff \
    less \
    && rm -rf /var/lib/apt/lists/*

# Link python3 to python
RUN ln -s /usr/bin/python3 /usr/bin/python

# Instale o AWS CLI
RUN pip3 install --no-cache-dir awscli

# Verifique a instalação do AWS CLI
RUN aws --version

RUN ./mvnw dependency:resolve

COPY ./app/src ./src

EXPOSE 8080

LABEL authors="augustoribeiro"

# Comando para executar o aplicativo Java (substitua com sua classe principal)
CMD ["./mvnw", "spring-boot:run"]