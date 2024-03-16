FROM maven:3.9.6-eclipse-temurin-17-focal AS build
WORKDIR /home/app
COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/dhiren/cloud/CloudSpringServiceApplication.java /home/app/src/main/java/com/dhiren/cloud/CloudSpringServiceApplication.java
RUN mvn -f /home/app/pom.xml clean package
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18.0-slim
COPY --from=build /home/app/target/*.jar /home/app/app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]
