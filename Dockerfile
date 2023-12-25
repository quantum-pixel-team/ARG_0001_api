FROM gradle:8.5-jdk17 AS build
WORKDIR /app
RUN chown -R gradle:gradle /app
USER gradle
COPY build.gradle .
COPY *gradle* .
RUN gradle dependencies
COPY src src
RUN gradle build


FROM gradle:8.5-jdk17
ARG build_version
COPY --from=build /app/build/libs/e-commarce-${build_version}.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "api.jar"]