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

build auth
build cart
build catalog
build frontend
build order
build stock
