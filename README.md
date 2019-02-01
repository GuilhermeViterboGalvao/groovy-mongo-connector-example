# Groovy Script with MongoDB

# <img src="DOCs/groovy.png" width="100" height="100"/> + <img src="DOCs/mongo.png" width="100" height="100"/>

## What is this ?
This is a simple example how to run a script Groovy with MongoDB.

## Installing MongoDB
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

## Create the database for tutorial

* 1-) Connect on MongoDB and execute these commands
```bash
$: mongo
> use user;
> db.createUser({ user: "user-admin", pwd: "admin123", roles: [ "readWrite" ] })
```

## Run the tutorial script

* 1-) Just run the import.sh script :-)
```bash
./import.sh
```