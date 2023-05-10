#!/bin/bash

echo "Compiling the NestJS Parking system within a multi-stage docker build"

docker build -t parking-service .

