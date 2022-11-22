FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} BullishAPI.jar
ENTRYPOINT ["java","-jar","/BullishAPI.jar"]