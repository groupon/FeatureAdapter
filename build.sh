#!/bin/bash
# make the script fail if any command fails
set -e
set -o pipefail

echo "Building FeatureAdapter"
./gradlew clean build
