FROM achwie/alpine-kafka:0.1

LABEL author="Achim Wiedemann"
LABEL description="An image with customized Kafka startup options (via ENV variables)"

ADD files/start.sh /

CMD ["/bin/bash", "/start.sh"]

