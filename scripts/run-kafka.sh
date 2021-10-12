#!/bin/bash

#Set this value to the path of kafka local installation
KAFKA_INSTALL_PATH="<local directory path where kafka is installed>"

if [[ -f zookeeper.pid ]]; then
  echo "Zookeeper.pid file exists"
  exit 0
elif [[ -f kafkaServer.pid ]]; then
  echo "Kafka Server File Exists"
  exit 0
fi

${KAFKA_INSTALL_PATH}/bin/zookeeper-server-start.sh ${KAFKA_INSTALL_PATH}/config/zookeeper.properties &
echo $! >>zookeeper.pid
sleep 15s
${KAFKA_INSTALL_PATH}/bin/kafka-server-start.sh ${KAFKA_INSTALL_PATH}/config/server.properties &
echo $! >>kafkaServer.pid