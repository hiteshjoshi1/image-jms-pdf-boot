#!/bin/bash

echo 'Creating Volume Mount for Docker...'
mkdir -p /home/${USER}/jarloc
cd  producer
echo 'Building Producer...'
mvn clean package -DskipTests

cd ../consumer
echo 'Building consumer...'
mvn clean package -DskipTests


cd ../
echo 'Copying .....'

cp producer/target/producer-0.0.1-SNAPSHOT.jar /home/${USER}/jarloc
cp consumer/target/consumer-0.0.1-SNAPSHOT.jar /home/${USER}/jarloc
echo 'All engines up, ready to launch to hyperspace.... '

docker-compose up
