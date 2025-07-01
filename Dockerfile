# builder image
FROM amazoncorretto:17-al2-jdk AS builder

RUN mkdir /Omni-BE-User
WORKDIR /Omni-BE-User

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar

# runtime image
FROM amazoncorretto:17.0.12-al2

ENV TZ=Asia/Seoul
ENV PROFILE=${PROFILE}

RUN mkdir /Omni-BE-User
WORKDIR /Omni-BE-User

COPY --from=builder /Omni-BE-User/build/libs/Omni-BE-User-* /Omni-BE-User/app.jar

CMD ["sh", "-c", " \
    java -Dspring.profiles.active=${PROFILE} \
         -jar /Omni-BE-User/app.jar"]

#FROM amazoncorretto:17-alpine3.18 as builder-jre
#
#RUN apk add --no-cache --repository=http://dl-cdn.alpinelinux.org/alpine/edge/main/ binutils=2.41-r0
#
#RUN $JAVA_HOME/bin/jlink \
#         --module-path "$JAVA_HOME/jmods" \
#         --verbose \
#         --add-modules ALL-MODULE-PATH \
#         --strip-debug \
#         --no-man-pages \
#         --no-header-files \
#         --compress=2 \
#         --output /jre
#
##=========================================================================
#
## syntax=docker/dockerfile:1
#FROM maven:3.8.5-openjdk-17 as build
#MAINTAINER Ahn Seungkyu
#
#WORKDIR /app
#COPY . /app
#RUN --mount=type=cache,target=/root/.m2 mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true package
#
##=========================================================================
#
#FROM alpine:3.18.4
#MAINTAINER Ahn Seungkyu
#
#ENV JAVA_HOME=/jre
#ENV PATH="$JAVA_HOME/bin:$PATH"
#ARG JAR_FILE=example-0.0.1-SNAPSHOT.jar
#
#COPY --from=builder-jre /jre $JAVA_HOME
#
#ARG APPLICATION_USER=appuser
#RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER
#
#RUN mkdir /app && chown -R $APPLICATION_USER /app
#
#USER 1000
#
#COPY --chown=1000:1000 --from=build /app/target/${JAR_FILE} /app/app.jar
#COPY scripts/run.sh /app/
#
#WORKDIR /app
#EXPOSE 8080
#
#ENTRYPOINT ["./run.sh"]