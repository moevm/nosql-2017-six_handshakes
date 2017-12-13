# Run with docker

You need to download docker-compose.yml. 

run ```docker pull evnikitina/6shakes```

then run in folder with **docker-compose.yml** ```docker-compose up```

Neo4j needs initial configuration. Just go to **localhost:7474** and set credentials: 

```
username: neo4j 
password: test
``` 

After all this steps you can access application via **localhost:8080**


Notice, that you need docker-compose version 2

## Local build

If you want to build image by yourself, download Dockerfile and run ``` docker build -t "evnikitina/6shakes:latest" <path to Dockerfile>``` 
