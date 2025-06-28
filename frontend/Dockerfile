FROM node:20-alpine AS build

WORKDIR /app

# Copy package.json first for cache
COPY package*.json ./
RUN npm install

# Then copy the rest of the app
COPY . .
RUN npm run build

EXPOSE 80