#!/bin/bash

instance_type_default="kafka"
kafka_port_default="9092"
kafka_zk_connect_default="localhost:2181"

if [[ "$1" == "--help" ]]; then
	echo "Usage: $0 [ INSTANCE_TYPE [ KAFKA_PORT [ KAFKA_ZK_CONNECT ] ] ]"
	echo "    INSTANCE_TYPE:    What to start inside the container, one of [ kafka | zookeeper ] (default: $instance_type_default)"
	echo "    KAFKA_PORT:       Kafka listen port (default: $kafka_port_default)"
	echo "    KAFKA_ZK_CONNECT: Zookeeper socket (default: $kafka_zk_connect_default)"
	exit 0
fi

instance_type=${1:-$instance_type_default}
kafka_listeners="PLAINTEXT://:${2:-$kafka_port_default}"
kafka_zk_connect="${3:-$kafka_zk_connect_default}"


docker run -it --rm \
	-e "INSTANCE_TYPE=$instance_type" \
	-e "KAFKA_CONFIG_LISTENERS=$kafka_listeners" \
	-e "KAFKA_CONFIG_ZK_CONNECT=$kafka_zk_connect" \
	achwie/kafka-test:0.2
