FROM gradle:jdk16
LABEL maintainer="xfileFIN"
WORKDIR /src
COPY . /src

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
