FROM ubuntu:16.04

ENV JAR "microservices-shop-demo-auth.jar"

RUN apt-get update && apt-get install -y \
	openjdk-8-jre

COPY "context/$JAR" /app/

# Using bash to be able to use env vars
CMD ["/bin/bash", "-c", "java -jar /app/$JAR"]
