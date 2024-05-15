#FROM amazoncorretto:21
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#CMD ["./mvnw", "spring-boot:run"]

ARG my_maven

FROM ${my_maven}
COPY ./src ./src
RUN mvn clean
RUN mvn package

ENTRYPOINT ["java", "-jar", "/opt/app/target/optics-0.0.1-SNAPSHOT.jar"]

#FROM maven:3.9.6-eclipse-temurin-21-alpine
#WORKDIR /opt/app
#COPY --from=build ./opt/app/target/*.jar /opt/app/app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/opt/app/pack-0.0.1-SNAPSHOT.jar"]