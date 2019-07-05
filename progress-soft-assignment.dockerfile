FROM openjdk:8u131-jre-alpine

WORKDIR /app

ARG artifactid
ARG version

ENV artifact ${artifactid}-${version}.war

COPY ./target/${artifact} app.war

RUN chmod +x /app/app.war

CMD ["/usr/bin/java", "-jar", "app.war"]

EXPOSE 8080


