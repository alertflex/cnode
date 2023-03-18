#!/bin/bash

#####################################################
# Technical project data for Alertflex Central node #
#####################################################

export INSTALL_PATH=/home/alertflex/cnode
export PROJECT_PATH=/home/alertflex/

# Settings for connection to ActiveMQ broker, 
# The Common Name (AKA CN) represents the server name protected by the SSL certificate.
# NOTE! CN is the Fully Qualified Domain Name (FQDN), do not use IP address for CN
# NOTE! password policy require to use a not short length of the password
export AMQ_CN=alertflex
export AMQ_USER=admin
export AMQ_PWD=*****

# web admin user and password for Alertflex Web console, Glassfish and ActiveMQ 
# NOTE! please, do not change ADMIN_USER (keeps the user name as admin)
export ADMIN_HOST=localhost
export ADMIN_USER=admin
export ADMIN_PWD=*****

# user and password for MySQL database (Alertflex and Afevents DB)
# password DB_PWD also will be used as root password for MySQL
# NOTE! some of MySQL password policy require to use password special char in password for example - ! or &. 
# Installation procedures can be interrupted if the password does not suite the policy
export DB_HOST=127.0.0.1
export DB_PORT=3306
export DB_USER=admin
export DB_PWD=*****

# install Alertflex Management console
export INSTALL_MC=no