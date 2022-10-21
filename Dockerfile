FROM openjdk:11-jdk

LABEL maintainer="mooh2jj"

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", \
"-Dspring.profiles.active=dev", \
"-jar", \
"/app.jar"]
