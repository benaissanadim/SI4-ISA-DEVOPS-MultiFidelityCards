#!/bin/bash

echo "Compiling the NestJS Notifier system within a multi-stage docker build"

docker build -t notifier-service .

