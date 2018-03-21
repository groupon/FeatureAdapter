#!/bin/bash
# print every command
set -x
# make the script fail if any command fails
set -e
set -o pipefail

# temporary solution to fix build tools issue
export PATH=$PATH:$ANDROID_HOME/tools/bin/
BUILD_TOOLS_VERSION=$(grep buildTools build.gradle | cut -d\' -f4)
yes | sdkmanager --update && sdkmanager "build-tools;${BUILD_TOOLS_VERSION}"


echo "Building FeatureAdapter"
./gradlew clean build
