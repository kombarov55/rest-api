#!/bin/bash

## Сборка через мавен: установить хост и порт параметры при запуске.
## Параметры:
##
## --dbhost: ip адрес базы
## --dbport: порт базы

mvn package
java -jar target/carx-jar-with-dependencies.jar --dbhost localhost --dbport 27017