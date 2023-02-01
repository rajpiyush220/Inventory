cat cred.txt | docker login --username devtouchblankspot --password-stdin
docker build -t devtouchblankspot/99mall_inventory:latest .
docker push devtouchblankspot/99mall_inventory:latest