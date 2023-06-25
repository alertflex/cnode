#!/bin/bash

# load technical project data for Alertflex cnode
source ./env.sh
export GLASSFISH_PATH=/opt/payara5
export PROJECT_NAME=csm_solution

CURRENT_PATH=`pwd`
if [[ $INSTALL_PATH != $CURRENT_PATH ]]
then
    echo "Please change install directory"
    exit 0
fi

echo "*** Installation Alertflex cnode started***"

# create project work directory 
sudo mkdir -p /opt
sudo mkdir -p $PROJECT_PATH
sudo mkdir -p $PROJECT_PATH/reports
sudo cp ./reports/alerts_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep1.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep2.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep3.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep4.jasper $PROJECT_PATH/reports/
sudo cp ./reports/misconfig_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/misconfig_app.jasper $PROJECT_PATH/reports/
sudo cp ./reports/misconfig_docker.jasper $PROJECT_PATH/reports/
sudo cp ./reports/misconfig_host.jasper $PROJECT_PATH/reports/
sudo cp ./reports/misconfig_k8s.jasper $PROJECT_PATH/reports/
sudo cp ./reports/vuln_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/vuln_app.jasper $PROJECT_PATH/reports/
sudo cp ./reports/vuln_docker.jasper $PROJECT_PATH/reports/
sudo cp ./reports/vuln_host.jasper $PROJECT_PATH/reports/
sudo cp ./reports/vuln_k8s.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanners_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_zap.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_kubehunter.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_nmap.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_nuclei.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_ins.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_gd.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_cform.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_terra.jasper $PROJECT_PATH/reports/

sudo mkdir -p $PROJECT_PATH/filters
sudo cp ./configs/enterprise-attack.json $PROJECT_PATH/

#set project id
export PROJECT_ID=$(cat /proc/sys/kernel/random/uuid)

# install java 
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0x219BD9C9
sudo apt-add-repository -y 'deb http://repos.azulsystems.com/ubuntu stable main'
sudo apt-get update
sudo apt-get -y install zulu-8 unzip

echo "*** Installation Nginx and web site ***"
sudo apt-get -y install nginx
sudo adduser --system --no-create-home --shell /bin/false --group --disabled-login nginx
sudo cp -r ./html/* /usr/share/nginx/html
sudo chown -R www-data:www-data /usr/share/nginx/html
sudo mkdir /etc/nginx/ssl
sudo chmod 700 /etc/nginx/ssl/
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/ssl/nginx.key -out /etc/nginx/ssl/nginx.crt -subj "/CN=$ADMIN_HOST"
sudo sed -i "s/_admin_host/$ADMIN_HOST/g" ./configs/default.conf
sudo sed -i "s/_admin_host/$ADMIN_HOST/g" ./html/api.yaml
sudo cp $INSTALL_PATH/configs/default.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/ssl.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/nginx.conf /etc/nginx/
sudo service nginx restart

echo "*** Installation Maven ***"
sudo apt-get -y install maven

echo "*** Installation Mysql ***"
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-apt-config"  | sudo tee /etc/apt/sources.list.d/mysql.list
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-5.7"  | sudo tee -a /etc/apt/sources.list.d/mysql.list
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-tools"  | sudo tee -a /etc/apt/sources.list.d/mysql.list
echo "deb-src http://repo.mysql.com/apt/ubuntu/ bionic mysql-5.7"  | sudo tee -a /etc/apt/sources.list.d/mysql.list

echo "Package: mysql-server" | sudo tee /etc/apt/preferences.d/mysql
echo "Pin: version 5.7*" | sudo tee -a /etc/apt/preferences.d/mysql
echo "Pin-Priority: 1001" | sudo tee -a /etc/apt/preferences.d/mysql
echo "" | sudo tee -a /etc/apt/preferences.d/mysql
echo "Package: mysql-client" | sudo tee -a /etc/apt/preferences.d/mysql
echo "Pin: version 5.7*" | sudo tee -a /etc/apt/preferences.d/mysql
echo "Pin-Priority: 1001" | sudo tee -a /etc/apt/preferences.d/mysql

sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 467B942D3A79BD29
sudo apt update

export DEBIAN_FRONTEND=noninteractive
sudo -E apt-get -q -y install mysql-server mysql-client
sudo bash -c "echo 'sql_mode=\"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\"' >> /etc/mysql/mysql.conf.d/mysqld.cnf"
sudo sed -i "s|_project_id|$PROJECT_ID|g" ./configs/alertflex.sql
sudo sed -i "s|_project_name|$PROJECT_NAME|g" ./configs/alertflex.sql
sudo sed -i "s|_project_path|$PROJECT_PATH|g" ./configs/alertflex.sql
sudo sed -i "s/_project_user/$ADMIN_USER/g" ./configs/alertflex.sql
sudo sed -i "s/_project_pwd/$ADMIN_PWD/g" ./configs/alertflex.sql
sudo sed -i "s/_db_host/$DB_HOST/g" ./configs/mysql.sql
sudo sed -i "s/_db_user/$DB_USER/g" ./configs/mysql.sql
sudo sed -i "s/_db_pwd/$DB_PWD/g" ./configs/mysql.sql
sudo mysql < ./configs/mysql.sql
sudo mysql -u root -p$DB_PWD alertflex < ./configs/alertflex.sql
sudo mysql -u root -p$DB_PWD afevents < ./configs/afevents.sql
sudo mysql -u root -p$DB_PWD misp < ./configs/misp.sql

echo "*** Installation Activemq ***"
sudo useradd -m activemq -d /opt/activemq
wget https://archive.apache.org/dist/activemq/5.16.1/apache-activemq-5.16.1-bin.tar.gz
tar xvzf apache-activemq-5.16.1-bin.tar.gz
sudo mv ./apache-activemq-5.16.1 /opt/activemq
sudo ln -snf /opt/activemq/apache-activemq-5.16.1 /opt/activemq/current

cd $INSTALL_PATH
sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" ./configs/jetty-realm.properties
sudo cp ./configs/jetty-realm.properties /opt/activemq/apache-activemq-5.16.1/conf

sudo sed -i "s/_amq_user/$AMQ_USER/g" ./configs/activemq.xml
sudo sed -i "s/_amq_pwd/$AMQ_PWD/g" ./configs/activemq.xml
sudo sed -i "s/_amq_key/$AMQ_PWD/g" ./configs/activemq.xml
sudo cp ./configs/activemq.xml /opt/activemq/apache-activemq-5.16.1/conf/activemq.xml

sudo rm /opt/activemq/apache-activemq-5.16.1/conf/broker.ks
sudo rm /opt/activemq/apache-activemq-5.16.1/conf/broker.ts
sudo rm /opt/activemq/apache-activemq-5.16.1/conf/client.ks
sudo rm /opt/activemq/apache-activemq-5.16.1/conf/client.ts

sudo keytool -genkey -noprompt -alias broker -keyalg RSA -dname "CN=$AMQ_CN" -keystore /opt/activemq/apache-activemq-5.16.1/conf/broker.ks -storepass $AMQ_PWD -keypass $AMQ_PWD
sudo keytool -importkeystore -noprompt -srckeystore /opt/activemq/apache-activemq-5.16.1/conf/broker.ks -destkeystore /opt/activemq/apache-activemq-5.16.1/conf/broker_cert.p12 -srcstoretype jks -srcstorepass $AMQ_PWD -deststoretype pkcs12 -deststorepass $AMQ_PWD
sudo openssl pkcs12 -in /opt/activemq/apache-activemq-5.16.1/conf/broker_cert.p12 -out $PROJECT_PATH/Broker.pem -password pass:$AMQ_PWD -passout pass:$AMQ_PWD

sudo ln -snf  /opt/activemq/current/bin/activemq /etc/init.d/activemq
sudo update-rc.d activemq defaults
sudo chown -R activemq:users /opt/activemq/apache-activemq-5.16.1


echo "*** Installation Glassfish/Payara AS ***"
cd /opt
sudo wget https://search.maven.org/remotecontent?filepath=fish/payara/distributions/payara/5.2020.5/payara-5.2020.5.zip
sudo mv *5.2020.5.zip payara-5.2020.5.zip
sudo unzip payara-5.2020.5.zip
sudo rm payara-5.2020.5.zip

sudo wget --no-check-certificate http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.35.zip
sudo unzip mysql-connector-java-5.1.35.zip mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar
sudo cp mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar $GLASSFISH_PATH/glassfish/domains/domain1/lib/ext/

cd $INSTALL_PATH

echo "* Starting Glassfish just to make sure all is well *"
sudo $GLASSFISH_PATH/bin/asadmin start-domain

echo "* Installation the start/stop scripts for Glassfish *"
sudo $GLASSFISH_PATH/bin/asadmin create-service

echo AS_ADMIN_PASSWORD= > password.txt
echo AS_ADMIN_NEWPASSWORD=$ADMIN_PWD >> password.txt
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER change-admin-password

echo AS_ADMIN_PASSWORD=$ADMIN_PWD > password.txt
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER enable-secure-admin

# Enable a listener port so that you can front Glassfish with Apache
#./asadmin create-network-listener --protocol http-listener-1 --listenerport 8009 --jkenabled true jk-connector

echo "* Creating Connection Pool *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-connection-pool\
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
  --restype javax.sql.XADataSource \
  --property PortNumber=${DB_PORT}:Password=${DB_PWD}:User=${DB_USER}:ServerName=${DB_HOST}:DatabaseName=alertflex alertflex_auth
  
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-connection-pool\
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
  --restype javax.sql.XADataSource \
  --property PortNumber=${DB_PORT}:Password=${DB_PWD}:User=${DB_USER}:ServerName=${DB_HOST}:DatabaseName=afevents afevents_auth

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-connection-pool\
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
  --restype javax.sql.XADataSource \
  --property PortNumber=${DB_PORT}:Password=${DB_PWD}:User=misp:ServerName=${DB_HOST}:DatabaseName=misp misp_auth

echo "* Creating JDBC Resource *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-resource --connectionpoolid alertflex_auth jdbc/alertflex_auth_jndi
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-resource --connectionpoolid afevents_auth jdbc/afevents_auth_jndi
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-jdbc-resource --connectionpoolid misp_auth jdbc/misp_auth_jndi

echo "* Creating JDBC Realm *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-auth-realm \
--classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm \
--property user-name-column=userid:password-column=password:group-name-column=groupid:\
jaas-context=jdbcRealm:datasource-jndi="jdbc/alertflex_auth_jndi":group-table=groups:\
user-table=users:digestrealm-password-enc-algorithm=AES:digest-algorithm=SHA-256:encoding=Hex:charset=UTF-8 JDBCRealm

echo "* Installion ActiveMQ resource *"
sudo wget https://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/5.16.1/activemq-rar-5.16.1.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy --type rar --name activemq-rar ./activemq-rar-5.16.1.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-resource-adapter-config --threadpoolid thread-pool-1 --property ServerUrl=\"tcp\\://localhost\\:61616\":UserName=$AMQ_USER:Password=$AMQ_PWD activemq-rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-connection-pool --raname activemq-rar --connectiondefinition javax.jms.ConnectionFactory --ping true jms/activeMQConnectionFactory-Connection-Pool
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-resource --poolname jms/activeMQConnectionFactory-Connection-Pool --enabled true jms/activeMQConnectionFactory

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties Alertflex=$PROJECT_NAME
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUrl="tcp\\://localhost\\:61616"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUser=$AMQ_USER
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqPwd=$AMQ_PWD

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  delete-jvm-options '-Xms512m'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  delete-jvm-options '-Xmx512m'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-jvm-options '-Xms3g'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-jvm-options '-Xmx3g'

echo "* Installion Alertflex applications *"  
cd $INSTALL_PATH

sudo mvn package

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy controller/target/alertflex-ctrl.war

if [[ $INSTALL_MC == yes ]]
then
	sudo curl -LO "https://github.com/alertflex/cnode/releases/download/v1.0.89/alertflex-mc.war"
	sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy alertflex-mc.war
fi

sudo /etc/init.d/payara_domain1 restart

echo "*** clean env ***"
# rm password.txt
# rm -r ./configs

echo "Project ID:"
echo $PROJECT_ID
echo "* Please, copy project id and reboot the cnode *"
exit 0
