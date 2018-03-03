#!/bin/bash

kafka_dir="/opt/kafka_2.11-1.0.0"

docker run --rm achwie/alpine-kafka:0.1 $kafka_dir/bin/kafka-server-start.sh $kafka_dir/config/server.properties
