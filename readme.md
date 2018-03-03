# About

Demo project for using Apache Kafka in a small and simplified microservices shop. It's a copy of the [Microservices Shop Demo project](http://github.com/achwie/microservices-shop-demo).

Some services make use of a common configuration file `src/main/resources/application.properties` in the module `kafka-demo-service-urls`. This file doesn't exist initially but there's already a template file `application.properties.default` in that directory which you can simply copy and rename to `application.properties`. Then everything should start up just fine.
