#!/bin/bash
source ../_common-functions.sh

docker run -it --rm achwie/kafka-demo-${service_name}:${service_version} java -jar ${service_name}-service.jar
