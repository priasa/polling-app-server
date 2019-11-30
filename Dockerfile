FROM openjdk:11
LABEL maintainer="ardi.priasa@gmail.com"
USER root
VOLUME /tmp
VOLUME /log
EXPOSE 5000
ADD target/polls-1.0.3.jar polls-1.0.3.jar
ENTRYPOINT ["java","-jar","polls-1.0.3.jar"]