FROM adoptopenjdk/openjdk11:alpine-jre

# Add Maintainer Info
LABEL maintainer="fernando_lomonaco@outlook.com"

# The application's jar file
ARG JAR_FILE=target/grillo*.jar

RUN echo "Copying the Grillo jar to the container..."
COPY ${JAR_FILE} grillo.jar

RUN echo "Copying config files"
COPY config/docker config/
COPY static/banner-docker.txt static/

# Make port 8080 available to the world outside this container
EXPOSE 8080

#ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n"

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom --spring.config.location=classpath:file:/config/docker/", "-jar", "/grillo.jar"]