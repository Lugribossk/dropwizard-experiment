#!/bin/bash
curl --version
curl -d '{"test": "test"}' localhost
curl -sS -d '{"test": "test"}' localhost