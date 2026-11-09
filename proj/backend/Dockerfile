# Stage 1: build the jar with the Maven wrapper, so no local Java or Maven is needed.
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Download dependencies in their own layer, so a code change does not download them again.
RUN sh ./mvnw -B -q dependency:go-offline
COPY src/ src/
# Tests run in CI (integration tests need Docker, not available inside this build).
RUN sh ./mvnw -B -q package -DskipTests

# Stage 2: run the jar on a JRE only, as a non-root user.
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
