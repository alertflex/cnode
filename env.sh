#!/bin/bash

####################################
# Technical project data for Cnode #
####################################

export INSTALL_PATH=/home/alertflex/cnode
export PROJECT_PATH=/home/alertflex/

# Settings for connection to ActiveMQ broker, 
# The Common Name (AKA CN) represents the server name protected by the SSL certificate
export AMQ_CN=xxxxx
export AMQ_USER=xxxxx
export AMQ_PWD=*****

# web admin user and password for Alertflex Web console, Glassfish and ActiveMQ 
# NOTE! please, do not change ADMIN_USER (keep user name as admin)
export ADMIN_HOST=xxxxx
export ADMIN_USER=admin
export ADMIN_PWD=*****

# user and password for MySQL database (Alertflex and Afevents DB)
# password DB_PWD also will be used as root password for MySQL and MISP
# NOTE! some of MySQL password policy require to use password special char in password for example - ! or &. 
# Installation procedures can be interrupted if the password does not suite the policy
export DB_HOST=xxxxx
export DB_PORT=3306
export DB_USER=xxxxx
export DB_PWD=*****

###########################
# install add-on packages #
###########################

# install in appliance configuration - altprobe and wazuh manager
export INSTALL_ALTPROBE=no

# MISP parameters, for MISP_URL use ip or url a hostname the same as for ALERTFLEX_HOST
export INSTALL_MISP=yes
export MISP_URL='xxxxx:8443'
export MISP_GPG='xxxxxxxxxxxxxxxxxxxxxxxxx'

export INSTALL_GRAFANA=yes
export INSTALL_NMAP=no
export INSTALL_ZAP=no

















