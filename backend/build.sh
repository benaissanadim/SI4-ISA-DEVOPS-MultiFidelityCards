#!/bin/bash

echo "Compiling the TCF Spring BACKEND within a multi-stage docker build"

mvn clean package


docker build --build-arg JAR_FILE=target/MultiFidelityCard-Backend-0.0.1-SNAPSHOT.jar -t spring-backend .


