FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-service-simple.jar
COPY ${JAR_FILE} pets-service.jar
ENTRYPOINT ["java","-jar", \
"/pets-service.jar"]
# Environment variables to be prdvided in docker-compose
