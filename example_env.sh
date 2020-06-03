#!/bin/bash

#########################################
# Technical project data for Controller #
#########################################

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
# NOTE! password DB_PWD also will be used as root password for MySQL and MISP
export DB_HOST=localhost
export DB_PORT=3306
export DB_USER=admin
export DB_PWD=Password1234

# MISP parameters, use for url a hostname the same as for ALERTFLEX_HOST
export MISP=true
export MISP_URL='af-ctrl:8443'
export MISP_GPG='XuJBao5Q2bps89LWFqWkKg'
