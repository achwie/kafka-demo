#!/bin/bash

source_dir="../target/"
source_jar="kafka-demo-auth-0.1-SNAPSHOT.jar"

target_dir="context/"
target_jar="kafka-demo-auth.jar"


mkdir -p "$target_dir" 
cp "$source_dir/$source_jar" "$target_dir/$target_jar"
docker build -t shop-auth .
