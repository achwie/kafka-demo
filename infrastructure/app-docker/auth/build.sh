#!/bin/bash
source ../_common-functions.sh

copy_jar_and_build_docker_image "$service_name" "$service_version"
