#!/bin/bash
curl -X POST \
-d '{"test": "test"}' \
localhost

TEST=test
curl -X POST \
-d '{"test": "'"${TEST}"'"}' \
localhost