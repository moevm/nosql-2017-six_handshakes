# nosql-2017-six_handshakes

You need running Neo4J server to use this application.<br>
If your neo4j credentials aren't like this 

```
username: neo4j 
password: root
``` 
change it in **application.yml** file 


# Run with docker

You need to download Dockerfile and docker-compose.yml. 

run ``` docker build -t "evnikitina/6shakes:latest" <path to Dockerfile>``` 

### Or
Download ```docker-compose.yml```

```docker pull evnikitina/6shakes```

then run in folder with **docker-compose.yml** ```docker-compose up```

Neo4j needs initial configuration. Just go to **localhost:7474** and set credentials: 

```
username: neo4j 
password: test
``` 

After all this steps you can access application via **localhost:8080**


Notice, that you need docker-compose version 2
