FROM maven:latest
MAINTAINER "Ekaterina Nikitina <katyanic@mail.ru>"

#install git

RUN apt-get update && apt-get install -y git wget
RUN git clone https://github.com/moevm/nosql-2017-six_handshakes.git
RUN cd nosql-2017-six_handshakes && git fetch && git checkout e0c1caa19f80d6ad4e2ccda4402aa7b7fdf39bbc
RUN cd nosql-2017-six_handshakes/back-end && chmod +x gradlew 
RUN cd nosql-2017-six_handshakes/back-end && cp -R gradle ../front-end/ && cp gradlew ../front-end
RUN cd nosql-2017-six_handshakes/front-end && ./gradlew build
RUN cd nosql-2017-six_handshakes/back-end && ./gradlew build
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "nosql-2017-six_handshakes/back-end/build/libs/six-handshakes-0.1.jar"]
