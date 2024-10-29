#!/bin/bash

#
# /**
# *@Author Akbar Riyan Nugroho
# */
#

echo "Build .."
./gradlew bootBuildImage
echo "RUN .."
docker stop beapi-dss
docker rm beapi-dss
docker run -d -it --restart=always --name=beapi-dss --ip 172.17.0.5 -p 9898:8080 be-dss:0.0.1-SNAPSHOT
