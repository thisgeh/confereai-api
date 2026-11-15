
FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /opt/app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar
USER app
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prd
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
CMD ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
