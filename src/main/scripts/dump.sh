#!/bin/sh
cd `dirname "$0"` && java -jar ${project.build.finalName}.jar "$@"
