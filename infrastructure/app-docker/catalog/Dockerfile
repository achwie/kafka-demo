FROM achwie/alpine-java:0.2

LABEL author="Achim Wiedemann"
LABEL description="Kafka Demo App - Catalog Service"

ADD dockerfiles/catalog-service.jar /

CMD ["java", "-jar", "/catalog-service.jar"] 

