FROM openjdk:jdk-alpine

# Use to set heap, trust store or other system properties.
COPY . .

RUN apk -U add alpine-sdk
RUN apk add --no-cache bash
WORKDIR gs-spring-boot
RUN ./gradlew build

EXPOSE 8080

CMD java ${JVM_OPTS} -jar build/libs/gs-spring-boot-0.1.0.jar
