# About

Demo project for using Apache Kafka in a small and simplified microservices shop. It's a copy of the [Microservices Shop Demo project](http://github.com/achwie/microservices-shop-demo).

Some services make use of a common configuration file `src/main/resources/application.properties` in the module `kafka-demo-service-urls`. This file doesn't exist initially but there's already a template file `application.properties.default` in that directory which you can simply copy and rename to `application.properties`. Then everything should start up just fine.

# Services

-Explain the services-

## Cart
Holds the current shopping cart for a user. Uses the session ID as a shopping cart ID to allow anonymous users to start shopping. 

The API to interact with the shopping cart can be found in the class `achwie.shop.cart.CartController`. It provides REST endpoints to read from the shopping cart and message driven endpoints to write to the shopping cart via Apache Kafka.

For Kafka I'm using [type mapping](https://docs.spring.io/spring-kafka/reference/html/#serdes-mapping-types) for deserialization of multiple types. In order to be able to implement `@KafkaListener`s for multiple types, I've placed the [annotation on the class](https://docs.spring.io/spring-kafka/docs/2.1.x/reference/html/_reference.html#class-level-kafkalistener) and used `@KafkaHandler` on the according methods in the class. This way, my `@KafkaListener` can handle different message types.


## Order
The order service has two main classes depending on which event transport you want to use:

* Use `OrderStarterLocal` to use an in-memory event transport
* Use `OrderStarteKafka` to use Apache Kafka (default)

The default main class is set in the `pom.xml` of the order module.
