# ---- Build Stage ----
FROM gradle:8.10.2-jdk21-alpine AS builder
WORKDIR /workspace

# Build-Definitionen für Cache nutzen
COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle ./gradle

# Projektquellen kopieren und final bauen
COPY . .

# Dependencies vorwärmen (Tests optional überspringen)
RUN gradle --no-daemon clean bootJar

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine

ENV TZ=UTC \
    JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=default

# Non-root User
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# App-JAR kopieren
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

COPY data /app/data
# Datenverzeichnis anlegen und Berechtigungen setzen
RUN chown -R app:app /app

# Volume für persistente Daten
VOLUME ["/app/data"]

USER app

# Exponierter Port (entsprechend App-Konfiguration)
EXPOSE 8081

# Startkommando
ENTRYPOINT ["sh", "-c" ,"java $JAVA_OPTS -Dspring.profiles.active=prod -jar /app/app.jar"]