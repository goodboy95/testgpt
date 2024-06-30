#!/bin/bash
mvn clean package
mv target/girlfriend-1.0.0.jar output/girlfriend.jar
cp src/main/resources/application.yml output/application.yml
