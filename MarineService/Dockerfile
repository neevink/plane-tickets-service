#define base docker image
FROM openjdk:17
LABEL maintainer="Egor Mitrofanov"
ADD target/MarineService-1.0-SNAPSHOT.jar MarineService.jar
ENTRYPOINT ["java", "-jar", "MarineService.jar"]