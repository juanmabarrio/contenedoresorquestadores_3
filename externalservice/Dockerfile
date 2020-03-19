FROM node:10

# App directory
WORKDIR /usr/app

# Copy dependencies file
COPY package*.json ./

# npm ci is better for packaging
RUN npm ci

# Copy app code
COPY . .

EXPOSE 8080

CMD [ "node", "./src/server.js"]