# Stage 1. Build.
FROM node:20-alpine AS builder
WORKDIR /app
COPY frontend/package-lock.json ./
COPY frontend/package.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build

# Stage 2. Runtime
FROM nginx:alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY frontend/nginx.conf /etc/nginx/conf.d/app.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD [ "nginx", "-g", "daemon off;" ]