#!/bin/sh

mvn flyway:migrate
mvn generate-sources