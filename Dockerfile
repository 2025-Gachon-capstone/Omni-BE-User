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
