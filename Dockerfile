############################
# 1) Build backend (Spring Boot jar)
############################
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /src

COPY Backend/pom.xml Backend/pom.xml
COPY Backend/src Backend/src

WORKDIR /src/Backend
RUN mvn -q -DskipTests package


############################
# 2) Build frontend (Vite -> static dist)
############################
FROM node:20-alpine AS frontend-build
WORKDIR /src/Frontend

COPY Frontend/package.json Frontend/package-lock.json ./
RUN npm ci

COPY Frontend/ ./
RUN npm run build


############################
# 3) Runtime (single container: nginx + java)
############################
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN apk add --no-cache nginx supervisor \
  && mkdir -p /run/nginx \
  && rm -rf /var/cache/apk/*

# Backend jar
COPY --from=backend-build /src/Backend/target/*.jar /app/backend.jar

# Frontend static assets + nginx config
COPY --from=frontend-build /src/Frontend/dist/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/http.d/default.conf

# Supervisor config to run both processes
COPY supervisord.conf /etc/supervisord.conf

EXPOSE 80

CMD ["supervisord", "-c", "/etc/supervisord.conf"]
