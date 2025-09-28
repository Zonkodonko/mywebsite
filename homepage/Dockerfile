ARG NODE_VERSION=24.7.0-alpine
ARG NGINX_VERSION=alpine3.22
#BUILD
FROM node:${NODE_VERSION} AS build
WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration=production

#RUN
FROM nginxinc/nginx-unprivileged:${NGINX_VERSION} AS runner
COPY --from=build /app/dist/homepage/browser /usr/share/nginx/html

COPY nginx.conf  /etc/nginx/conf.d/default.conf

EXPOSE 80




