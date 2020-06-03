#!/bin/bash
mysql -u misp --password=$MYSQL_MISP_PASSWORD -h $MYSQL_MISP_HOST -P 3306 misp < /var/www/MISP/INSTALL/MYSQL.sql





