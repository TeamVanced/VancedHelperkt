FROM openjdk:15
LABEL maintainer="xfileFIN"

RUN apt-get update && apt-get install openjdk-15-jre

CMD gradlew run
