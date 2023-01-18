FROM openjdk:17-alpine

LABEL maintainer="rajpiyush220@gmail.com"

WORKDIR /workspace/app

COPY config/secret.yaml /workspace/app/config/secret.yaml

COPY build/libs/Inventory-1.0.jar /workspace/app/inventory.jar

ENTRYPOINT [ "java", "-jar" , "/workspace/app/inventory.jar" ]
