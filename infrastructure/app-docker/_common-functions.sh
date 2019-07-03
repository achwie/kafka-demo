#
# Common functions for Docker build 
#



# Declare some convenience variables
service_name=$(basename $(pwd))
service_version="0.2"



function copy_jar_and_build_docker_image {
  local service_name="$1"
  local service_version="$2"

  # Name of the JAR in the docker container
  local service_jar="${service_name}-service.jar"
  
  # Assuming there's only on .jar file produced by Maven
  local maven_jar="../../../${service_name}/target/"*.jar
  
  # This dirname will also be used in the Dockerfile to ADD the jar
  local files_dir="dockerfiles"
  
  mkdir -p "$files_dir"
  cp $maven_jar "$files_dir/$service_jar"
  docker build -t "achwie/kafka-demo-${service_name}:${service_version}" .
  rm -rf "$files_dir"
}
