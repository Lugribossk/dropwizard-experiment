#!/bin/bash
# Redeploys a Tutum service with the newest version of it's associated Docker image, and checks that it starts correctly.
# Also sends deployment status to Github.
SERVICE_ID=$1 # Tutum ID of the service to redeploy.
DEPLOY_ENVIRONMENT=$2 # Deployment environment name to report to Github.

REPO_NAME=${CIRCLE_PROJECT_USERNAME}/${CIRCLE_PROJECT_REPONAME}

github () {
    URL=$1 # URL after repo name to post to.
    PAYLOAD=$2 # JSON payload as a string.

	curl -X POST \
	-H "Accept: application/vnd.github.v3+json" \
	-H "Authorization: token "${GITHUB_TOKEN} \
	-H "Content-Type: application/json" \
	-H "User-Agent: Lugribossk deploy script" \
	-d ${PAYLOAD} \
	https://api.github.com/repos/${REPO_NAME}/${URL}
}

githubDeployStatus () {
    ID=$1 # Github deployment ID.
    STATE=$2 # Github deployment state, either "pending", "success", "failure" or "error".

    github deployments/${ID}/statuses '{"state": "'"${STATE}"'", "target_url": "https://circleci.com/gh/'"${REPO_NAME}"'/'"${CIRCLE_BUILD_NUM}"'"}'
}

tutumPublicUrl () {
    ID=$1

    tutum service inspect ${ID} | grep -Po '"public_dns": "\K([^"]*)'
}

DEPLOYMENT_ID=$(github deployments '{"ref": "'"${CIRCLE_SHA1}"'", "description": "CircleCI", "environment": "'"${DEPLOY_ENVIRONMENT}"'"}' |  grep -Po '"url":"[^,]*?deployments/\K([^"]*)')
githubDeployStatus ${DEPLOYMENT_ID} pending

tutum service redeploy ${SERVICE_ID}
SERVICE_HEALTHCHECK_URL=$(tutumPublicUrl ${SERVICE_ID})/admin/healthcheck

if curl --retry 12 --retry-delay 5 --no-buffer ${SERVICE_HEALTHCHECK_URL} ; then
    githubDeployStatus ${DEPLOYMENT_ID} success
else
    githubDeployStatus ${DEPLOYMENT_ID} failure
fi
