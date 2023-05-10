#!/bin/bash

# postgres
docker run --name postgresql-local -p 127.0.0.1:5432:5432 -e POSTGRES_USER=postgresuser -e POSTGRES_PASSWORD=postgrespass -e POSTGRES_DB=db -d postgres:15.2

