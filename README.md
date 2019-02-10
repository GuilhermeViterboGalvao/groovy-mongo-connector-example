# Groovy Script with MongoDB

# <img src="DOCs/groovy.png" width="100" height="100"/> + <img src="DOCs/mongo.png" width="100" height="100"/>

## What is this ?
This is a simple example how to run a script Groovy with MongoDB.

### What you will need to run this project ?

* [Java](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
```bash
sudo apt-get install java
```

* [Groovy](http://groovy-lang.org/download.html)
```bash
sudo apt-get install groovy
```

* [Maven](https://maven.apache.org/download.cgil)
```bash
sudo apt-get install maven
```

* [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
* [Docker-Compose](https://docs.docker.com/compose/install/s)

### Run with Docker

* 1-) Run the docker compose build in the folder script, example:
```bash
docker-compose build
```

* 2-) Run the setup step on the docker compose, examploe:
```bash
docker-compose run --rm setup
```

* 3-) Run the ETL inscript inside of conteiner:
```bash
docker-compose run --rm app bash
$:> nohup ./run_for_docker.sh &
$:> tail -f logs/user.log
```

## Running at your machine, installing MongoDB
This installation example it's only for Linux.

* 1-) Add apt-key and install MongoDB

```bash
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
sudo apt-get update
sudo apt-get install -y mongodb-org
```

* 2-) Creating directories

```bash
sudo mkdir -p /data/db/
sudo chmod -R 775 /data/
```

* 3-) Creating the service file

```bash
sudo touch /etc/systemd/system/mongodb.service
sudo nano /etc/systemd/system/mongodb.service
``` 

Paste this into mongodb.service file

```text
[Unit]
Description=High-performance, schema-free document-oriented database
After=network.target

[Service]
User=mongodb
ExecStart=/usr/bin/mongod --quiet --config /etc/mongod.conf

[Install]
WantedBy=multi-user.target
```

* 4-) Start the service

```bash
sudo systemctl start mongodb
sudo systemctl status mongodb
```

## Creating the database for tutorial

* 1-) Connect on MongoDB and execute these commands
```bash
$: mongo
> use user;
> db.createUser({ user: "user-admin", pwd: "admin123", roles: [ "readWrite" ] })
```

## Run the tutorial script

* 1-) Just run the import.sh script :-)
```bash
./run.sh
```