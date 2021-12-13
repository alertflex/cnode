#!/bin/bash

# load technical project data for Alertflex controller
source ./env.sh
export GLASSFISH_PATH=/opt/payara5
export PROJECT_NAME=csm_solution

CURRENT_PATH=`pwd`
if [[ $INSTALL_PATH != $CURRENT_PATH ]]
then
    echo "Please change install directory"
    exit 1
fi

echo "*** Installation Alertflex controller started***"

sudo bash -c 'echo "Defaults timestamp_timeout=50" >> /etc/sudoers.d/99_sudo_include_file'

sudo yum -y install epel-release unzip wget
sudo yum -y update

# create project work directory 
sudo mkdir -p /opt
sudo mkdir -p $PROJECT_PATH
sudo mkdir -p $PROJECT_PATH/reports
sudo cp ./reports/alerts_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep1.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep2.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep3.jasper $PROJECT_PATH/reports/
sudo cp ./reports/alerts_subrep4.jasper $PROJECT_PATH/reports/
sudo cp ./reports/containers_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/containers_dockerbench.jasper $PROJECT_PATH/reports/
sudo cp ./reports/containers_kubebench.jasper $PROJECT_PATH/reports/
sudo cp ./reports/containers_kubehunter.jasper $PROJECT_PATH/reports/
sudo cp ./reports/containers_trivy.jasper $PROJECT_PATH/reports/
sudo cp ./reports/endpoints_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/endpoints_misconfig.jasper $PROJECT_PATH/reports/
sudo cp ./reports/endpoints_vuln.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanners_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_zap.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_dependency.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_sonarqube.jasper $PROJECT_PATH/reports/
sudo cp ./reports/scanner_snyk.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_report.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_ins.jasper $PROJECT_PATH/reports/
sudo cp ./reports/cloud_gd.jasper $PROJECT_PATH/reports/

sudo mkdir -p $PROJECT_PATH/filters
sudo cp ./configs/enterprise-attack.json $PROJECT_PATH/

# create firewall rules
sudo firewall-cmd --add-service=ntp
sudo firewall-cmd --add-service=http
sudo firewall-cmd --add-service=https
sudo firewall-cmd --add-port=1514/udp
sudo firewall-cmd --add-port=3000/tcp
sudo firewall-cmd --add-port=3306/tcp
sudo firewall-cmd --add-port=4848/tcp
sudo firewall-cmd --add-port=8181/tcp
sudo firewall-cmd --add-port=8443/tcp
sudo firewall-cmd --add-port=61617/tcp
sudo firewall-cmd --runtime-to-permanent

#set project id
export PROJECT_ID=$(cat /proc/sys/kernel/random/uuid)

echo "*** install java  ***"
sudo rpm --import http://repos.azulsystems.com/RPM-GPG-KEY-azulsystems
sudo curl -o /etc/yum.repos.d/zulu.repo http://repos.azulsystems.com/rhel/zulu.repo
sudo yum -y install zulu-8

echo "*** Installation Nginx and web site ***"
sudo yum -y install nginx
sudo rm -f /usr/share/nginx/html/img
sudo cp -r ./html/* /usr/share/nginx/html/
sudo git clone https://github.com/alertflex/doc.git /usr/share/nginx/html/doc
sudo setsebool -P httpd_can_network_connect 1
sudo mkdir /etc/nginx/ssl
sudo chmod 700 /etc/nginx/ssl/
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/ssl/nginx.key -out /etc/nginx/ssl/nginx.crt -subj "/CN=$ADMIN_HOST"
sudo sed -i "s/_admin_host/$ADMIN_HOST/g" ./configs/default.conf
sudo cp $INSTALL_PATH/configs/default.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/ssl.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/nginx.conf /etc/nginx/
sudo systemctl start nginx
sudo systemctl enable nginx

echo "*** Installation Maven ***"
sudo yum -y install maven

echo "*** Installation Mysql ***"
sudo yum -y localinstall https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
sudo yum -y install mysql-community-server
sudo systemctl start mysqld
sudo systemctl enable mysqld

# Get the temporary DB password
temp_password=$(sudo grep password /var/log/mysqld.log | awk 'NR==1{print $NF}')
sudo bash -c "echo \"ALTER USER 'root'@'localhost' IDENTIFIED BY '$DB_PWD'; flush privileges;\"" > reset_pass.sql
sudo mysql -u root --password="$temp_password" --connect-expired-password < reset_pass.sql

sudo bash -c "echo 'sql_mode=\"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\"' >> /etc/my.cnf"

sudo sed -i "s|_project_id|$PROJECT_ID|g" ./configs/alertflex.sql
sudo sed -i "s|_project_name|$PROJECT_NAME|g" ./configs/alertflex.sql
sudo sed -i "s|_project_path|$PROJECT_PATH|g" ./configs/alertflex.sql
sudo sed -i "s/_project_user/$ADMIN_USER/g" ./configs/alertflex.sql
sudo sed -i "s/_project_pwd/$ADMIN_PWD/g" ./configs/alertflex.sql
sudo sed -i "s/_db_host/$DB_HOST/g" ./configs/alertflex.sql
sudo sed -i "s/_db_user/$DB_USER/g" ./configs/alertflex.sql
sudo sed -i "s/_db_pwd/$DB_PWD/g" ./configs/alertflex.sql

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
	sudo bash -c "echo 'bind-address = 0.0.0.0' >> /etc/my.cnf"
	sudo systemctl stop mysqld
	sudo systemctl start mysqld
	sudo curl -fsSL https://get.docker.com/ | sh
	sudo systemctl start docker
	sudo systemctl enable docker
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
cd /opt
FILE=apache-activemq-5.16.1-bin.tar.gz
sudo wget https://archive.apache.org/dist/activemq/5.16.1/$FILE
if [[ -f "$FILE" ]]
then
    echo "$FILE exist"
else 
    echo "$FILE does not exist"
	exit 1
fi
sudo tar xvzf $FILE
sudo ln -s /opt/apache-activemq-5.16.1 /opt/activemq

sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" $INSTALL_PATH/configs/jetty-realm.properties
sudo cp $INSTALL_PATH/configs/jetty-realm.properties /opt/activemq/conf

sudo sed -i "s/_amq_user/$AMQ_USER/g" $INSTALL_PATH/configs/activemq.xml
sudo sed -i "s/_amq_pwd/$AMQ_PWD/g" $INSTALL_PATH/configs/activemq.xml
sudo sed -i "s/_amq_key/$AMQ_PWD/g" $INSTALL_PATH/configs/activemq.xml
sudo cp $INSTALL_PATH/configs/activemq.xml /opt/activemq/conf/activemq.xml
	
sudo rm /opt/activemq/conf/*.ks
sudo rm /opt/activemq/conf/*.ts
sudo rm /opt/activemq/conf/*.cert

sudo keytool -genkey -noprompt -alias broker -keyalg RSA -dname "CN=$AMQ_CN" -keystore /opt/activemq/conf/broker.ks -storepass $AMQ_PWD -keypass $AMQ_PWD
sudo keytool -importkeystore -noprompt -srckeystore /opt/activemq/conf/broker.ks -destkeystore /opt/activemq/conf/broker_cert.p12 -srcstoretype jks -srcstorepass $AMQ_PWD -deststoretype pkcs12 -deststorepass $AMQ_PWD
sudo openssl pkcs12 -in /opt/activemq/conf/broker_cert.p12 -out $PROJECT_PATH/Broker.pem -password pass:$AMQ_PWD -passout pass:$AMQ_PWD

sudo bash -c 'cat << EOF > /usr/lib/systemd/system/activemq.service
[Unit]
Description=activemq message queue
After=network.target
[Service]
PIDFile=/opt/activemq/data/activemq.pid
ExecStart=/opt/activemq/bin/activemq start
ExecStop=/opt/activemq/bin/activemq stop
User=root
Group=root
[Install]
WantedBy=multi-user.target
EOF'

sudo systemctl enable activemq.service
sudo systemctl start activemq.service

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
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-resource-adapter-config --threadpoolid thread-pool-1 --property ServerUrl=\"tcp://localhost:61616\":UserName=$AMQ_USER:Password=$AMQ_PWD activemq-rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-connection-pool --raname activemq-rar --connectiondefinition javax.jms.ConnectionFactory --ping true jms/activeMQConnectionFactory-Connection-Pool
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER create-connector-resource --poolname jms/activeMQConnectionFactory-Connection-Pool --enabled true jms/activeMQConnectionFactory

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties Alertflex=$PROJECT_NAME
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUrl="tcp\\://localhost\\:61616"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqUser=$AMQ_USER
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties AmqPwd=$AMQ_PWD
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties RedisHost=127.0.0.1
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER  create-system-properties RedisPort=6379

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
	sudo curl -LO "https://github.com/alertflex/cnode/releases/download/v1.0.2/alertflex-mc.war"
	sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user $ADMIN_USER deploy alertflex-mc.war
fi

sudo /etc/init.d/payara_domain1 restart

echo "*** clean env ***"
rm password.txt
rm reset_pass.sql
rm -r ./configs
sudo rm /etc/sudoers.d/99_sudo_include_file

echo "Please, reboot the controller"
exit 0



