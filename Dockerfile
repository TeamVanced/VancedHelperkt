FROM gradle:jdk15
LABEL maintainer="xfileFIN"
WORKDIR /src
COPY . /src

USER root
RUN chown -R gradle /src
RUN chmod +x /src/gradlew
USER gradle

CMD ./gradlew run
