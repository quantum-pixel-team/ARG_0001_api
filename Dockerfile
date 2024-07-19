FROM  amazoncorretto:21-alpine AS build
WORKDIR /app
COPY build.gradle .
COPY *gradle* ./
COPY gradle gradle/
RUN chmod +x ./gradlew && \
    ./gradlew dependencies
COPY src src
RUN ./gradlew build
RUN java -Djarmode=layertools -jar build/libs/api.jar extract

FROM  amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/spring-deps/ ./
COPY --from=build /app/dependencies/ ./
COPY --from=build /app/hibernate-deps/ ./
COPY --from=build /app/spring-boot-loader/ ./
COPY --from=build /app/snapshot-dependencies/ ./
COPY --from=build /app/application/ ./

COPY src/main/java/com/quantum_pixel/arg/utilities/HealthCheck.java .
EXPOSE 8080

HEALTHCHECK --interval=10s \
    CMD java HealthCheck.java localhost 8080 /actuator/health

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]