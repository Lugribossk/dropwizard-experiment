#!/bin/bash
set -e
# Builds and pushes a Docker image.

NAME=todo-server
FOLDER=./todo/todo-server
REGISTRY=tutum.co
FULL_IMAGE_NAME=${REGISTRY}/${DOCKER_USERNAME}/${NAME}:${CIRCLE_BRANCH}

echo Building Docker image ${FULL_IMAGE_NAME}
docker build -t ${FULL_IMAGE_NAME} ${FOLDER}
docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD} -e ${DOCKER_EMAIL} ${REGISTRY}
docker push ${FULL_IMAGE_NAME}