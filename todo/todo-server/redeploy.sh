#!/bin/bash
SERVICE_ID=$1

REPO_NAME=${CIRCLE_PROJECT_USERNAME}/${CIRCLE_PROJECT_REPONAME}

github () {
    URL=$1
    PAYLOAD=$2

	curl -X POST \
	-H "Accept: application/vnd.github.v3+json" \
	-H "Authorization: token "${GITHUB_TOKEN}
	-H "Content-Type: application/json" \
	-H "User-Agent: Lugribossk deploy script" \
	-d ${PAYLOAD} \
	https://api.github.com/repos/${REPO_NAME}/${URL}
}

githubDeployStatus () {
    ID=$1
    STATUS=$2
    DESCRIPTION=$3

    github deployments/${ID}/statuses '{"status": "'"${STATUS}"'", "description": "'"${DESCRIPTION}"'", "target_url": "https://circleci.com/gh/'"${REPO_NAME}"'/'"${CIRCLE_BUILD_NUM}"'"}'
}

DEPLOYMENT_ID=$(github deployments '{"ref": "'"${CIRCLE_SHA1}"'", "environment": "production", "description": "CircleCI"}' | ./JSON.sh | egrep '\["id","[^"]*"\]')
githubDeployStatus ${DEPLOYMENT_ID} pending "Deployment in progress..."

tutum service redeploy ${SERVICE_ID}
SERVICE_ENDPOINT_URL=$(tutum service inspect ${SERVICE_ID} | ./JSON.sh | egrep '\["blahurl","[^"]*"\]')

if curl --retry 12 --retry-delay 5 --no-buffer ${SERVICE_ENDPOINT_URL} ; then
    githubDeployStatus ${DEPLOYMENT_ID} success "Deployment successful."
else
    githubDeployStatus ${DEPLOYMENT_ID} failure "Deployment failed!"
fi
