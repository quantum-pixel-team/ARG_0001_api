FROM  amazoncorretto:17-alpine AS build
WORKDIR /app

HEALTHCHECK --interval=10s --start-period=30s \
    CMD java -cp /app/classes/java/main com.quantum_pixel.arg.utilities.HealthCheck localhost 8080 /actuator/health


ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","libs/api.jar"]
