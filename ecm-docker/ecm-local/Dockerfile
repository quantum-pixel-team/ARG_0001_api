FROM  amazoncorretto:17-alpine AS build
WORKDIR /app

HEALTHCHECK --interval=10s \
    CMD java classes/java/main/com/quantum_pixel/ecm/utilities/HealthCheck.java localhost 8080 /actuator/health

ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","libs/api.jar"]
