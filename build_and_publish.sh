cd server

docker build -t mastercloudapps/pa-server:1.0 .
docker push mastercloudapps/pa-server:1.0

cd ..

cd externalservice

docker build -t mastercloudapps/pa-externalservice:1.0 .
docker push mastercloudapps/pa-externalservice:1.0

cd ..

cd worker

docker build -t mastercloudapps/pa-worker:1.0 .
docker push mastercloudapps/pa-worker:1.0

cd ..