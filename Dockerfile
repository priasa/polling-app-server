FROM java:8
LABEL maintainer="ardi.priasa@gmail.com"
VOLUME /tmp
EXPOSE 8080
ADD target/polls-1.0.0.jar polls-1.0.0.jar
ENTRYPOINT ["java","-jar","polls-1.0.0.jar"]