FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mybank-springboot-app.jar
ENTRYPOINT ["java","-jar","/mybank-springboot-app.jar"]