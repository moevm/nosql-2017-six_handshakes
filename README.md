# Run with docker

You need to download Dockerfile and docker-compose.yml. 

run ``` docker build -t "evnikitina/6shakes:latest" <path to Dockerfile>``` 

Now you can access application via **localhost:8080** and Neo4j Browser  via **localhost:7474** with following credentials:
```
username: neo4j 
password: test
```
**Notice, that you need docker-compose version 2**

