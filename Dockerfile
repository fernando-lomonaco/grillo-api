FROM adoptopenjdk/openjdk11:alpine-jre

# Add Maintainer Info
LABEL maintainer="fernando_lomonaco@outlook.com"

# Add a volume pointing to /tmp
VOLUME /tmp

ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n"

# Environment
#ENV PROFILES_ACTIVE "docker"
ENV profile default

# The application's jar file
ARG JAR_FILE=target/grillo*.jar

RUN echo "Copying the Grillo jar to the container..."
COPY ${JAR_FILE} grillo-api.jar
# Copy the application's jar to the container
#COPY ${JAR_FILE} app.jar

RUN echo "Copying config files"
COPY config config

# Make port 8080 available to the world outside this container
EXPOSE 8080

# java -jar /opt/app/app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /grillo-api.jar", "--spring.config.location=file:///config/${profile}/application.properties"]

#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$PROFILES_ACTIVE -jar /app.jar" ]

