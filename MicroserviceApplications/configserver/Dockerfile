# A JRE (Java Runtime Environment) image is required because it provides the necessary components to run Java applications,
FROM openjdk:21-jdk-slim

# Add metadata (Recommended over MAINTAINER)
LABEL maintainer="piyushkumar9818@gmail.com"

# copies the JAR file from the first stage to the second stage.
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar

# This is necessary because containers don’t "boot" like a regular OS—they only run a single main process.
# Without this, the container would start and immediately exit, doing nothing.
ENTRYPOINT ["java","-jar","configserver-0.0.1-SNAPSHOT.jar"]


