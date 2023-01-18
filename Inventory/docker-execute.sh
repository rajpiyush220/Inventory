rm -rf build
./gradlew clean assemble
docker-compose down
docker-compose up -d