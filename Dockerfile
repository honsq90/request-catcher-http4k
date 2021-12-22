FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-alpine-slim
COPY build/distributions/RequestCatcher.zip RequestCatcher.zip
RUN unzip RequestCatcher.zip
EXPOSE 9000
CMD RequestCatcher/bin/RequestCatcher
