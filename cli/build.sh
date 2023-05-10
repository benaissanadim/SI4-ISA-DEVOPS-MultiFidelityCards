#!/bin/bash
echo "Compiling the TCF Spring CLI within a multi-stage docker build"

mvn clean package

docker build --build-arg JAR_FILE=target/MultiFidelityCard-Cli-0.0.1-SNAPSHOT.jar -t spring-cli .
