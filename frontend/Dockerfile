FROM node:22 AS builder
WORKDIR /app
COPY . .
COPY .env-prod .env
RUN npm install
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]