FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /app
COPY . /app
RUN mvn install

FROM openjdk:17.0.2-jdk
WORKDIR /app
ADD target/urlshortcut-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -jar app.jar
