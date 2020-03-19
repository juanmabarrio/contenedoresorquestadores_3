FROM maven:3.3-jdk-8 as builder
COPY ./pom.xml /code/
WORKDIR /code
RUN mvn verify clean --fail-never  dependency:go-offline dependency:resolve-plugins

# There is no clean way to get all needed dependencies for a successful maven build in offline mode. See https://stackoverflow.com/questions/43661755/how-to-build-and-release-a-maven-project-from-an-offline-machine-with-a-file-di

COPY . /code/
RUN mvn package -DskipTests=true -B

FROM openjdk:8-jre
COPY --from=builder /code/target/*.jar /usr/app/
WORKDIR /usr/app

CMD ["java", "-jar", "p3-worker-0.0.1-SNAPSHOT.jar"]
