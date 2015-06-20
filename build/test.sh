#!/bin/bash
set -x

REPO_NAME=${CIRCLE_PROJECT_USERNAME}/${CIRCLE_PROJECT_REPONAME}

github () {
    URL=$1 # URL after repo name to post to.
    PAYLOAD=$2 # JSON payload as a string.

	curl -X POST -sS \
	-H "Accept: application/vnd.github.v3+json" \
	-H "Authorization: token "${GITHUB_TOKEN} \
	-H "Content-Type: application/json" \
	-H "User-Agent: Lugribossk deploy script" \
	-d $(echo ${PAYLOAD}) \
	https://api.github.com/repos/${REPO_NAME}/${URL}
}

github deployments "{\"ref\": \"${CIRCLE_SHA1}\", \"description\": \"CircleCI\", \"required_contexts\": [], \"environment\": \"test\"}"