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
sudo cp ./reports/alertflex.png $PROJECT_PATH/reports/
sudo cp ./reports/alerts_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep1.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep2.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep3.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep4.jasper $PROJECT_PATH/reports/
sudo mkdir -p $PROJECT_PATH/filters
sudo mkdir -p $PROJECT_PATH/geo
sudo cp ./configs/GeoLiteCity.dat $PROJECT_PATH/geo/
sudo cp ./configs/enterprise-attack.json $PROJECT_PATH/

#set project id
export PROJECT_ID=$(cat /proc/sys/kernel/random/uuid)

# install java 
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 0x219BD9C9
sudo apt-add-repository 'deb http://repos.azulsystems.com/ubuntu stable main'
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
sudo cp $INSTALL_PATH/configs/default.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/ssl.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/nginx.conf /etc/nginx/
sudo service nginx restart

echo "*** Installation Maven ***"
sudo apt-get -y install maven

echo "*** installation Redis ***"
sudo apt-get -y install redis-server
sudo systemctl enable redis-server.service 

echo "*** Installation Mysql ***"
echo mysql-server mysql-server/root_password password $DB_PWD | sudo debconf-set-selections
echo mysql-server mysql-server/root_password_again password $DB_PWD | sudo debconf-set-selections
sudo apt-get -y install mysql-server mysql-client

sudo bash -c "echo 'sql_mode=\"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\"' >> /etc/mysql/mysql.conf.d/mysqld.cnf"

sudo sed -i "s|_project_id|$PROJECT_ID|g" ./configs/alertflex.sql
sudo sed -i "s|_project_name|$PROJECT_NAME|g" ./configs/alertflex.sql
sudo sed -i "s|_project_path|$PROJECT_PATH|g" ./configs/alertflex.sql
sudo sed -i "s/_project_user/$ADMIN_USER/g" ./configs/alertflex.sql
sudo sed -i "s/_project_pwd/$ADMIN_PWD/g" ./configs/alertflex.sql
sudo sed -i "s/_db_host/$DB_HOST/g" ./configs/alertflex.sql
sudo sed -i "s/_db_user/$DB_USER/g" ./configs/alertflex.sql
sudo sed -i "s/_db_pwd/$DB_PWD/g" ./configs/alertflex.sql

if [[ $INSTALL_ZAP == yes ]]
then
	echo "*** Installation OWASP ZAP ***"
	wget https://github.com/zaproxy/zaproxy/releases/download/v2.9.0/ZAP_2.9.0_Linux.tar.gz
	tar zxf ZAP_2.9.0_Linux.tar.gz
	sudo mv ./ZAP_2.9.0 /opt/zap
	
	sudo bash -c 'cat << EOF > /lib/systemd/system/zap.service
[Unit]
Description=OWASP ZAP Proxy
After=syslog.target network-online.target

[Service]
ExecStart=/opt/zap/zap.sh -daemon -host 127.0.0.1 -port 8090 -config api.disablekey=true
PrivateTmp=true

[Install]
WantedBy=default.target
EOF'
	sudo systemctl enable zap
	sudo sed -i "s/_zap_host/localhost/g" ./configs/alertflex.sql
else
	sudo sed -i "s/_zap_host/indef/g" ./configs/alertflex.sql
fi

if [[ $INSTALL_NMAP == yes ]]
then
	echo "*** Installation Nmap ***"
	sudo apt-get -y install nmap
fi

if [[ $INSTALL_GRAFANA == yes ]]
then
	echo "*** Installation Grafana ***"
	sudo apt-get install -y apt-transport-https
	sudo apt-get install -y software-properties-common wget
	wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -
	sudo add-apt-repository "deb https://packages.grafana.com/oss/deb stable main"
	sudo apt-get update
	sudo apt-get -y install grafana
	sudo systemctl daemon-reload
	sudo systemctl enable grafana-server.service
	sudo sed -i "s/_grafana_pwd/$DB_PWD/g" ./configs/grafana.ini
	sudo cp ./configs/grafana.ini /etc/grafana
	sudo sed -i "s/_db_pwd/$DB_PWD/g" ./configs/grafana-db.sql
	sudo mysql -u root -p$DB_PWD < ./configs/grafana-db.sql
	sudo sed -i "s/_db_user/$DB_USER/g" ./configs/datasource.yaml
	sudo sed -i "s/_db_pwd/$DB_PWD/g" ./configs/datasource.yaml
	sudo cp ./configs/datasource.yaml /etc/grafana/provisioning/datasources
	sudo mkdir /var/lib/grafana/dashboards
	sudo cp ./configs/dashboards/alertflex.yaml /etc/grafana/provisioning/dashboards
	sudo cp ./configs/dashboards/alert_dashboard.json /var/lib/grafana/dashboards
	sudo chown -R grafana:grafana /var/lib/grafana/dashboards
	sudo openssl pkcs12 -export -in /etc/nginx/ssl/nginx.crt -inkey /etc/nginx/ssl/nginx.key -out /etc/grafana/grafana.p12 -passout pass:
	sudo openssl pkcs12 -in /etc/grafana/grafana.p12 -nodes -out /etc/grafana/grafana.pem -passin pass:
	sudo cp /etc/nginx/ssl/nginx.key /etc/grafana/grafana.key
	sudo chown -R grafana:grafana /etc/grafana/grafana.key
	sudo chown -R grafana:grafana /etc/grafana/grafana.pem
fi


if [[ $INSTALL_MISP == no ]]
then
	sudo mysql -u root -p$DB_PWD < ./configs/alertflex.sql
	sudo sed -i "s/_mispdb_pwd/$DB_PWD/g" ./configs/misp-db.sql
	sudo mysql -u root -p$DB_PWD < ./configs/misp-db.sql
else
	sudo mysql -u root -p$DB_PWD < ./configs/alertflex.sql
	echo "*** Installation Docker and MISP container ***"
	sudo sed -i "s/_mispdb_pwd/$DB_PWD/g" ./misp/misp-db.sql
	sudo mysql -u root -p$DB_PWD < ./misp/misp-db.sql
	sudo sed -i "s/.*bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/mysql.conf.d/mysqld.cnf
	sudo /etc/init.d/mysql stop
	sudo /etc/init.d/mysql start
	sudo apt-get update
	sudo apt-get -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
	curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
	sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
	sudo apt-get update
	sudo apt-get -y install docker-ce docker-ce-cli containerd.io
	sudo docker network create --subnet 192.168.200.0/24 --gateway 192.168.200.1 misp-network
	sudo cp /etc/nginx/ssl/nginx.key $INSTALL_PATH/misp
	sudo cp /etc/nginx/ssl/nginx.crt $INSTALL_PATH/misp
	cd $INSTALL_PATH/misp
	sudo docker build \
	--build-arg MYSQL_MISP_HOST=192.168.200.1 \
	--build-arg MYSQL_MISP_PASSWORD=$DB_PWD \
	--build-arg MISP_FQDN=$MISP_URL \
	--build-arg MISP_GPG_PASSWORD=$MISP_GPG \
	-t alertflex/misp .
	sudo docker run -d --name misp -p 8443:443 --net misp-network --ip 192.168.200.2 --restart=always alertflex/misp
	sudo docker exec -i -e MYSQL_MISP_HOST=192.168.200.1 -e MYSQL_MISP_PASSWORD=$DB_PWD misp sh -c "/db-init.sh"
fi

echo "*** Installation Activemq ***"
sudo useradd -m activemq -d /opt/activemq
cd /opt/activemq
sudo wget https://archive.apache.org/dist/activemq/5.16.0/apache-activemq-5.16.0-bin.tar.gz
sudo tar xvzf apache-activemq-5.16.0-bin.tar.gz
sudo ln -snf apache-activemq-5.16.0 current
cd $INSTALL_PATH

sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" ./configs/jetty-realm.properties
sudo cp ./configs/jetty-realm.properties /opt/activemq/apache-activemq-5.16.0/conf

sudo sed -i "s/_amq_user/$AMQ_USER/g" ./configs/activemq.xml
sudo sed -i "s/_amq_pwd/$AMQ_PWD/g" ./configs/activemq.xml
sudo sed -i "s/_amq_key/$AMQ_PWD/g" ./configs/activemq.xml
sudo cp ./configs/activemq.xml /opt/activemq/apache-activemq-5.16.0/conf/activemq.xml
	
cd /opt/activemq/apache-activemq-5.16.0/conf

sudo rm *.ks
sudo rm *.ts
sudo rm *.cert

sudo keytool -genkey -noprompt -alias broker -keyalg RSA -dname "CN=$AMQ_CN" -keystore broker.ks -storepass $AMQ_PWD -keypass $AMQ_PWD
sudo keytool -importkeystore -noprompt -srckeystore broker.ks -destkeystore broker_cert.p12 -srcstoretype jks -srcstorepass $AMQ_PWD -deststoretype pkcs12 -deststorepass $AMQ_PWD
sudo openssl pkcs12 -in broker_cert.p12 -out $PROJECT_PATH/Broker.pem -password pass:$AMQ_PWD -passout pass:$AMQ_PWD

sudo ln -snf  /opt/activemq/current/bin/activemq /etc/init.d/activemq
sudo update-rc.d activemq defaults
sudo chown -R activemq:users /opt/activemq/apache-activemq-5.16.0

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
sudo wget https://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/5.16.0/activemq-rar-5.16.0.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy --type rar --name activemq-rar ./activemq-rar-5.16.0.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-resource-adapter-config --threadpoolid thread-pool-1 --property ServerUrl=\"tcp\\://localhost\\:61616\":UserName=$AMQ_USER:Password=$AMQ_PWD activemq-rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-connection-pool --raname activemq-rar --connectiondefinition javax.jms.ConnectionFactory --ping true jms/activeMQConnectionFactory-Connection-Pool
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-resource --poolname jms/activeMQConnectionFactory-Connection-Pool --enabled true jms/activeMQConnectionFactory

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties Alertflex=$PROJECT_NAME
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUrl="tcp\\://localhost\\:61616"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUser=$AMQ_USER
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqPwd=$AMQ_PWD
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties RedisHost=127.0.0.1
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties RedisPort=6379

sudo cp $INSTALL_PATH/configs/logback.xml $GLASSFISH_PATH/glassfish/domains/domain1/config/

echo "* Installion Alertflex applications *"  
cd $INSTALL_PATH
sudo mvn package
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy controller/target/alertflex-ctrl.war

if [[ $INSTALL_MC == yes ]]
then
	sudo curl -LO "https://github.com/alertflex/cnode/releases/download/0.701/alertflex-mc.war"
	sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy alertflex-mc.war
fi

echo "*** clean env ***"
rm password.txt
rm -r ./configs

echo "* Please, reboot the cnode *"
exit 0
