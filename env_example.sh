#!/bin/bash

####################################
# Technical project data for Cnode #
####################################

export INSTALL_PATH=/home/alertflex/cnode
export PROJECT_PATH=/home/alertflex/

# Settings for connection to ActiveMQ broker, 
# The Common Name (AKA CN) represents the server name protected by the SSL certificate.
# NOTE! CN is the Fully Qualified Domain Name (FQDN), do not use IP address for CN
# NOTE! password policy require to use a not short length of the password
export AMQ_CN=alertflex
export AMQ_USER=admin
export AMQ_PWD=Pwd12345

# web admin user and password for Alertflex Web console, Glassfish and ActiveMQ 
# NOTE! please, do not change ADMIN_USER (keep user name as admin)
export ADMIN_HOST=alertflex
export ADMIN_USER=admin
export ADMIN_PWD=Pwd12345

# user and password for MySQL database (Alertflex and Afevents DB)
# password DB_PWD also will be used as root password for MySQL and MISP
# NOTE! some of MySQL password policy require to use password special char in password for example - ! or &. 
# Installation procedures can be interrupted if the password does not suite the policy
export DB_HOST=localhost
export DB_PORT=3306
export DB_USER=admin
export DB_PWD=Pwd12345

export INSTALL_MC=yes






