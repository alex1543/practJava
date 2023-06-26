#!/usr/bin/env bash
MY_PATH="$(dirname -- "${BASH_SOURCE[0]}")"
echo "$MY_PATH"
echo "ok"
cd $MY_PATH
export CLASSPATH=/Library/Java/Extensions/mysql-connector-j-8.0.33.jar:$CLASSPATH
java Test.java
