#!/bin/bash

#########################################
# Technical project data for Controller #
#########################################

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
# NOTE! password DB_PWD also will be used as root password for MySQL and MISP
export DB_HOST=xxxxx
export DB_PORT=3306
export DB_USER=xxxxx
export DB_PWD=*****

# MISP parameters, use for url a hostname the same as for ALERTFLEX_HOST
export MISP=false
export MISP_URL='xxxxx:8443'
export MISP_GPG='xxxxxxxxxxxxxxxxxxxxxxxxx'












