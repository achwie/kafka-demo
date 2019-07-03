#!/bin/bash

kafka_dir="/opt/kafka_2.12-2.3.0"

docker run --rm achwie/alpine-kafka:0.2 $kafka_dir/bin/kafka-server-start.sh $kafka_dir/config/server.properties
