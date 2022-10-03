FROM amazoncorretto:11-alpine
COPY build/distributions/RequestCatcher.zip RequestCatcher.zip
RUN unzip RequestCatcher.zip
EXPOSE 9000
CMD RequestCatcher/bin/RequestCatcher
