#!/bin/bash
#
# Builds the docker images required in docker-compose.yml
#

function build {
	build_path="$1"

	pushd "$build_path"
	./build.sh
	popd
}

build alpine-java
build alpine-kafka
build kafka-test
