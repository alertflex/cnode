#!/bin/bash

##################################################
# Technical project data for Alertflex collector #
##################################################

# sources
# if *_LOG is "indef", redis connection will use
export FALCO_LOG=indef
export MODSEC_LOG=indef
export SURI_LOG=indef
export WAZUH_LOG='\/var\/ossec\/logs\/alerts\/alerts.json'

# install add-on packages
export INSTALL_REDIS=false
export REDIS_HOST=127.0.0.1
export INSTALL_FALCO=false
export INSTALL_SURICATA=false
export INTERFACE=xxx
export INSTALL_WAZUH=true
export WAZUH_HOST=127.0.0.1
export WAZUH_USER=foo
export WAZUH_PWD=bar











