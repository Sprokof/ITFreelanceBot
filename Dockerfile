FROM openjdk:21
MAINTAINER parmenid
WORKDIR /app
COPY ./target/FreelanceBot-1.0-SNAPSHOT-spring-boot.jar /app/FreelanceBot-1.0.0.jar
ENTRYPOINT ["java","-jar","/app/FreelanceBot-1.0.0.jar"]