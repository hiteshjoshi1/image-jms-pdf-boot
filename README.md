# Spring Boot Image - PDF App

## Git repo for Image to PDF APP converter


###  written by Hitesh Joshi - sendmailtojoshi@gmail.com

### What you can you do with the Application

- Sign IN, Log IN  
- Upload Image
- View common PDF
- Download common PDF
- View Uploaded Images

### Tech details
- Project is developed using Java 8, Spring boot, thymeleaf, Apache ActiveMQ, MongoDB.
- Uses Basic Spring Security  - credentials are stored after encryption using bCrypt in Mongodb
- Producer hosts the Web App
- Consumer is responsible for Image to PDF processing

### Build Steps

Using Docker

- Use a non root user with sudo access to run the project in linux/unix
- Clone
```
git clone https://github.com/hiteshjoshi1/image-jms-pdf-boot.gitit
```

```
cd image-jms-pdf-boot/
```

```
run ./install.sh
```

As of now we do not have a CI/CD pipeline, so I have created a install.sh script that builds the java projects and places them inside the shared volume.
The volume is shared between docker containers and can be accessed inside a container /data
Finally the script brings up the docker containers with
docker-compose up


Following Docker containers are created
-  2 Java containers based on my Java 8 Image at https://hub.docker.com/r/hiteshjoshi1/microservice-docker-cart-example/
- Consumer
- Producer
- ActiveMQ
- MongoDB
- We also join the containers in a network

## Producer Application
Application is written using Spring boot for backend and thymeleaf and HTML for UI rendering. We are overriding Spring boot managed version of bootstrap webjar and using Bootstrap 4.0.0 

## There are two Spring profiles
- dev  : default profile, running on windows , baseDir can be changed to anything the user wants in application.yml
- docker : Running in a docker container

Once application starts,Producer application is runnig at
http://localhost:3333/

First Time, please create an account
http://localhost:3333/registration

Use Account are saved in Mongodb with passowords hased with bcrypt
Since this is a demo app, we  have used basic Spring security with session.

## Consumer Application
- Consumer application has no user interface. it hosts JMS listeners which act on receiving Image Request from Producer
- Receive a message in Image queue
- Create a PDF copy of Image.
- Create JMS Message for PDF queue
- Receive the JMS message from PDF Queue
- Update the System Wide PDF

### Once you create an account, you can 

- Upload Images
- View YOUR Uploaded Images
- View System Wide PDF
- Download System Wide PDF
- Log Out
- Login Back again



Swagger Endpoint (Post Login)
http://localhost:3333/swagger-ui.html#/


In this somewhat contrived example , we are not using many essential features of microservices.
There is no service discovery as services are not dependent on each other. 
There is no circuit breaking or distributed logging infrastructure for the same reason.
There is no external config.

If you want these concepts in action in action, please checkout my other repo- 
https://github.com/hiteshjoshi1/microservice-docker-cart-example

It has  web service discovery using Netflix eureka.
Externalized config using Spring config.
Circuit breaker using Netflix Hysterix and Turbine.
Distributed log gathering using Spring sleuth and zipkin and RabbitMQ AMQP.
Edge proxy using Zuul


