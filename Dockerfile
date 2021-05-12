FROM gradle:jdk15
LABEL maintainer="xfileFIN"
WORKDIR /src
COPY . /src/build/libs/bot.jar

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
