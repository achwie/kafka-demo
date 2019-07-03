FROM achwie/alpine-java:0.2

LABEL author="Achim Wiedemann"
LABEL description="Kafka Demo App - Order Service"

ADD dockerfiles/order-service.jar /

CMD ["java", "-jar", "/order-service.jar"] 

