#!/bin/bash

# Se placer dans le répertoire /home/teamc
cd /home/teamc

# Exécuter docker-compose dans /home/teamc
echo "Starting containers in /home/teamc"
docker-compose up -d

# Se placer dans le répertoire /home/teamc/artifactory
cd /home/teamc/artifactory/artifactory-oss-7.49.8

# Exécuter docker-compose dans /home/teamc/artifactory
echo "Starting containers in /home/teamc/artifactory"
docker-compose up -d

# Se placer dans le répertoire /home/teamc/sonar
cd /home/teamc/sonar

# Exécuter docker-compose dans /home/teamc/sonar
echo "Starting containers in /home/teamc/sonar"
docker-compose up -d



docker exec -it agent /bin/bash -c "apt-get update && apt-get install -y ca-certificates"

# permisssion for directory /.m2 in agent
docker exec -it agent /bin/bash -c "chown -R 1000:1000 /home/jenkins/.m2"

# permission to jenkins to execute docker
sudo usermod -aG docker jenkins
sudo chmod 666 /var/run/docker.sock
