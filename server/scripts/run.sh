./scripts/wait-for-it.sh $RABBIT_MQ_HOST:15672 -t 0;
./scripts/wait-for-it.sh $MONGO_HOST:27017 -t 0; 

node ./src/server.js