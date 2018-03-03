#/bin/ bash

kafka_dir="/opt/kafka_2.11-1.0.0"
broker_id_default="0"
instance_type_default="kafka"
config_listeners_default="PLAINTEXT://:9092"
config_advertised_listeners_default="PLAINTEXT://:9092"
config_zk_connect_default="localhost:2181"

if [[ $1 == "--help" ]]; then
	echo "Starts Kafka or Zookeeper in $kafka_dir. Uses the following environment variables:"
	echo "  * BROKER_ID:                Which broker ID to use - must be numeric (default: $broker_id_default)"
	echo "  * INSTANCE_TYPE:            What to start - must be one of [ kafka | zookeeper] (default: $instance_type_default)"
	echo "  * KAFKA_CONFIG_LISTENERS:   overrides the Kafka property 'listeners' (default: $config_listeners_default)"
	echo "  * KAFKA_CONFIG_ADVERTISED_LISTENERS:   overrides the Kafka property 'advertised.listeners' (default: $config_advertised_listeners_default)"
	echo "  * KAFKA_CONFIG_ZK_CONNECT:  overrides the Kafka property 'zookeeper.connect' (default: $config_zk_connect_default)"
	echo ""
	echo "Usage: $0"
	exit 0
fi

broker_id="${BROKER_ID:-$broker_id_default}"
instance_type="${INSTANCE_TYPE:-$instance_type_default}"
config_listeners="${KAFKA_CONFIG_LISTENERS:-$config_listeners_default}"
config_advertised_listeners="${KAFKA_CONFIG_ADVERTISED_LISTENERS:-$config_advertised_listeners_default}"
config_zk_connect="${KAFKA_CONFIG_ZK_CONNECT:-$config_zk_connect_default}"

echo "Broker ID: $broker_id"
echo "Instance type: $instance_type"
echo "Listeners: $config_listeners"
echo "Advertised listeners: $config_advertised_listeners"
echo "ZK connect: $config_zk_connect"

pushd "$kafka_dir"

case $instance_type in
        kafka)
                "$kafka_dir/bin/kafka-server-start.sh" config/server.properties \
                        --override broker.id="$broker_id" \
                        --override listeners="$config_listeners" \
                        --override advertised.listeners="$config_advertised_listeners" \
                        --override zookeeper.connect="$config_zk_connect"
        ;;
        zookeeper)
                "$kafka_dir/bin/zookeeper-server-start.sh" config/zookeeper.properties
        ;;
        *)
                echo "ERROR: Given instance type is invalid! Must be one of [ kafka | zookeeper ]"
                exit 1
        ;;
esac

popd
