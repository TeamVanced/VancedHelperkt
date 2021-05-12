FROM gradle:jdk15
LABEL maintainer="xfileFIN"
WORKDIR /src
COPY . /src

COPY entrypoint.sh /entrypoint.sh
RUN ls -la
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
