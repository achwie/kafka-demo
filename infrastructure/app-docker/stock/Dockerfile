FROM achwie/alpine-java:0.1

LABEL author="Achim Wiedemann"
LABEL description="Kafka Demo App - Stock Service"

ADD dockerfiles/stock-service.jar /

CMD ["java", "-jar", "/stock-service.jar"] 

