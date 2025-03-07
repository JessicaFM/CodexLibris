brew services list
brew services stop postgresql
sudo lsof -i :5432


sudo kill -9 $(sudo lsof -t -i:5432)


docker exec -it api_container sh
apk add --no-cache postgresql-client
