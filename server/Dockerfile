FROM node:10

# App directory
WORKDIR /usr/app

# Copy dependencies file
COPY package*.json ./

# npm ci is better for packaging
RUN npm ci

# Copy app code
COPY . .

EXPOSE 9090

CMD ["bash", "-c", "./scripts/run.sh"]
