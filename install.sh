#!/bin/bash
sudo docker network create --driver bridge carx-net > /dev/null

sudo docker run -d --name mongo-server --network carx-net mongo
sudo docker run -it --network carx-net -p 80:80 carx-tz:latest
docker start mongo-server
docker start webapp

# Запросы для тестирования:
#curl localhost:80/sync -X PUT -d uuid=759C8248-B316-456A-911E-2020A0241E66 -d data='{money: 3000, country: "RU"}'
#curl localhost:80/sync?uuid=759C8248-B316-456A-911E-2020A0241E66
#curl localhost:80/activity -X POST -d uuid=759C8248-B316-456A-911E-2020A0241E66 -d activity=1