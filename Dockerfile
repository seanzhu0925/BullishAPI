FROM jenkins/jenkins
EXPOSE 9090:9090
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} BullishAPI.jar
ENTRYPOINT ["java","-jar","/BullishAPI.jar"]