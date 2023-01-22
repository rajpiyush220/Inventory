docker network create --driver bridge inventory_network || true
docker-compose -f docker-compose_db.yml up -d