FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon --no-build-cache

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/notificacao.jar

EXPOSE 8082

CMD ["java", "-jar", "notificacao.jar"]