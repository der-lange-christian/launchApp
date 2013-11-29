#!/bin/sh

# Installation für Ubuntu
#
# Quelle: http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html

sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java7-installer
# als default einstellen
sudo apt-get install oracle-java7-set-default

# Removing 
# sudo apt-get remove oracle-java7-installer
