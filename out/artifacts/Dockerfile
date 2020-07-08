FROM frolvlad/alpine-java:latest
VOLUME /gateway9098
ADD *.jar /gateway9098/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/gateway9098/gtauth.jar"]
