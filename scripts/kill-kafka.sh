#!/bin/bash

if [[ -f zookeeper.pid ]]; then
  echo "Killing Zookeeper"
  kill -9 $(cat zookeeper.pid)
  rm zookeeper.pid
fi

if [[ -f kafkaServer.pid ]]; then
  echo "Killing Kafka Server"
  kill -9 $(cat kafkaServer.pid)
  rm kafkaServer.pid
fi