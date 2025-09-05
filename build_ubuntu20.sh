#!/bin/bash

echo "*** Install Java Maven***"
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0x219BD9C9
sudo apt-add-repository -y 'deb http://repos.azulsystems.com/ubuntu stable main'
sudo apt-get update
sudo apt-get -y install zulu-8 maven unzip

echo "*** Build Controller ***"
sudo mvn package
