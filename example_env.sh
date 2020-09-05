#!/bin/bash

####################################
# Technical project data for Cnode #
####################################

export INSTALL_PATH=/home/alertflex/cnode
export PROJECT_PATH=/home/alertflex/

# Settings for connection to ActiveMQ broker, 
# The Common Name (AKA CN) represents the server name protected by the SSL certificate
export AMQ_CN=af-ctrl
export AMQ_USER=admin
export AMQ_PWD=Password1234

# web admin user and password for Alertflex Web console, Glassfish and ActiveMQ 
# NOTE! please, do not change ADMIN_USER (keep admin user name as admin)
export ADMIN_HOST=af-ctrl
export ADMIN_USER=admin
export ADMIN_PWD=Password1234

# user and password for MySQL database (Alertflex and Afevents DB)
# password DB_PWD also will be used as root password for MySQL and MISP
# NOTE! some of MySQL password policy require to use password special char in password for example - ! or &. 
# Installation procedures can be interrupted if the password does not suite the policy
export DB_HOST=localhost
export DB_PORT=3306
export DB_USER=admin
export DB_PWD=Password!1234

###########################
# install add-on packages #
###########################

# install in appliance configuration - altprobe and wazuh manager
export INSTALL_ALTPROBE=yes

# MISP parameters, for MISP_URL use ip or url a hostname the same as for ALERTFLEX_HOST
export INSTALL_MISP=yes
export MISP_URL='af-ctrl:8443'
export MISP_GPG='XuJBao5Q2bps89LWFqWkKg'

export INSTALL_GRAFANA=yes
export INSTALL_NMAP=yes
export INSTALL_ZAP=yes



