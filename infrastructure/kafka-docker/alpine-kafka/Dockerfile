FROM achwie/alpine-java:0.1

LABEL author="Achim Wiedemann"
LABEL description="An image with Kafka 1.0.0 (plus wget and bash)"

# Basic stuff (should go into it's own image)
RUN apk add --update wget bash

# Install bash and wget, download Kafka, install Kafka into /opt
RUN apk add --update bash wget && \
	wget -P /tmp http://ftp-stud.hs-esslingen.de/pub/Mirrors/ftp.apache.org/dist/kafka/1.0.0/kafka_2.11-1.0.0.tgz && \
	mkdir /opt && \
	tar -xzf /tmp/kafka_2.11-1.0.0.tgz -C /opt

