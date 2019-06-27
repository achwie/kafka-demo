# Infrastructure

Here you'll find two folders:

 * `kafka-docker`: contains Dockerfiles for a Java base image and a Kafka image
 * `app-docker`: contains the Dockerfiles for the business services (cart, order, etc.)

In both of the top-level infrastructure folders there's a convenience build script: `build-all.sh`. This will build all Docker images in the sub folders. You'll then be able to launch the respective setup via `docker-compose up`.

## kafka-docker

Contains Dockerfiles to build the basic images. If launched via `docker-compose up` it will create an environment with one ZooKeeper and three Kafka nodes. This can be handy if you plan to launch the business services from your IDE but want a Kafka runtime to connect to.

## app-docker

Contains Dockerfiles for building the business services. In order to do so, you'll have to complete two steps:

1. build the `alpine-java` image in `kafka-docker` (change into `kafka-docker` and run `build-all.sh`)
1. build the business services with Maven (go to the root folder of the project and run `mvn package`)

Now you have all the prerequisites to build and run the business services. Change into `app-docker` and run `build-all.sh`. Then launch the whole setup with `docker-compose up`.
