#!/bin/sh
rm -rf build/libs
./gradlew clean assemble
docker-compose build
docker-compose down
docker-compose up -d