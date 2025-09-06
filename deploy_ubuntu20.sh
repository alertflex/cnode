#!/bin/bash

# load technical project data
source ./.env.sh

export GLASSFISH_PATH=/opt/payara5
export INSTALL_PATH=`pwd`
export PROJECT_ID=$(cat /proc/sys/kernel/random/uuid)

echo "*** Installation Mysql ***"
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-apt-config"  | sudo tee /etc/apt/sources.list.d/mysql.list
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-5.7"  | sudo tee -a /etc/apt/sources.list.d/mysql.list
echo "deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-tools"  | sudo tee -a /etc/apt/sources.list.d/mysql.list
echo "deb-src http://repo.mysql.com/apt/ubuntu/ bionic mysql-5.7"  | sudo tee -a /etc/apt/sources.list.d/mysql.list
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys B7B3B788A8D3785C
sudo apt update
sudo apt install -y mysql-server=5.7.* mysql-client=5.7.*
sudo bash -c "echo 'sql_mode=\"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION\"' >> /etc/mysql/mysql.conf.d/mysqld.cnf"
sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" configs/init.sql
sudo mysql -u root -p$ADMIN_PWD mysql < configs/init.sql
sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" configs/alertflex.sql
sudo sed -i "s/_project_id/$PROJECT_ID/g" configs/alertflex.sql
sudo sed -i "s/_os_port/$OPENSEARCH_PORT/g" configs/alertflex.sql
sudo sed -i "s/_os_user/$OPENSEARCH_USER/g" configs/alertflex.sql
sudo sed -i "s/_os_pwd/$OPENSEARCH_PWD/g" configs/alertflex.sql
sudo sed -i "s|_os_url|$OPENSEARCH_URL|g" configs/alertflex.sql
sudo mysql -u root -p$ADMIN_PWD alertflex < configs/alertflex.sql
sudo mysql -u root -p$ADMIN_PWD afevents < configs/afevents.sql

echo "*** Installation Nginx and web site ***"
sudo apt-get -y install nginx
sudo adduser --system --no-create-home --shell /bin/false --group --disabled-login nginx
sudo cp -r ./html/* /usr/share/nginx/html
sudo chown -R www-data:www-data /usr/share/nginx/html
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/nginx.key -out /etc/nginx/nginx.crt -subj "/CN=$ADMIN_HOST"
sudo cp $INSTALL_PATH/configs/default.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/ssl.conf /etc/nginx/conf.d/
sudo cp $INSTALL_PATH/configs/nginx.conf /etc/nginx/
sudo service nginx restart

echo "*** Installation Activemq ***"
sudo useradd -m activemq -d /opt/activemq
wget https://archive.apache.org/dist/activemq/5.16.1/apache-activemq-5.16.1-bin.tar.gz
tar xvzf apache-activemq-5.16.1-bin.tar.gz
sudo mv ./apache-activemq-5.16.1 /opt/activemq
sudo ln -snf /opt/activemq/apache-activemq-5.16.1 /opt/activemq/current

cd $INSTALL_PATH
sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" ./configs/jetty-realm.properties
sudo cp ./configs/jetty-realm.properties /opt/activemq/apache-activemq-5.16.1/conf
sudo sed -i "s/_admin_pwd/$ADMIN_PWD/g" ./configs/activemq.xml
sudo cp ./configs/activemq.xml /opt/activemq/apache-activemq-5.16.1/conf/activemq.xml

sudo ln -snf  /opt/activemq/current/bin/activemq /etc/init.d/activemq
sudo update-rc.d activemq defaults
sudo chown -R activemq:users /opt/activemq/apache-activemq-5.16.1

echo "*** Installation Glassfish/Payara AS ***"
cd /opt
sudo wget https://search.maven.org/remotecontent?filepath=fish/payara/distributions/payara/5.2020.5/payara-5.2020.5.zip
sudo mv *5.2020.5.zip payara-5.2020.5.zip
sudo unzip payara-5.2020.5.zip
sudo rm payara-5.2020.5.zip

sudo wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.35/mysql-connector-java-5.1.35.jar
sudo cp mysql-connector-java-5.1.35.jar $GLASSFISH_PATH/glassfish/domains/domain1/lib/ext/

cd $INSTALL_PATH

echo "* Starting Glassfish just to make sure all is well *"
sudo $GLASSFISH_PATH/bin/asadmin start-domain

echo "* Installation the start/stop scripts for Glassfish *"
sudo $GLASSFISH_PATH/bin/asadmin create-service

echo AS_ADMIN_PASSWORD= > password.txt
echo AS_ADMIN_NEWPASSWORD=$ADMIN_PWD >> password.txt
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin change-admin-password

echo AS_ADMIN_PASSWORD=$ADMIN_PWD > password.txt
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin enable-secure-admin

echo "* Creating Connection Pool *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-jdbc-connection-pool\
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
  --restype javax.sql.XADataSource \
  --property PortNumber=3306:Password=${ADMIN_PWD}:User=admin:ServerName=localhost:DatabaseName=alertflex alertflex_auth
  
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-jdbc-connection-pool\
  --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
  --restype javax.sql.XADataSource \
  --property PortNumber=3306:Password=${ADMIN_PWD}:User=admin:ServerName=localhost:DatabaseName=afevents afevents_auth

echo "* Creating JDBC Resource *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-jdbc-resource --connectionpoolid alertflex_auth jdbc/alertflex_auth_jndi
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-jdbc-resource --connectionpoolid afevents_auth jdbc/afevents_auth_jndi

echo "* Creating JDBC Realm *"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-auth-realm \
--classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm \
--property user-name-column=userid:password-column=password:group-name-column=groupid:\
jaas-context=jdbcRealm:datasource-jndi="jdbc/alertflex_auth_jndi":group-table=groups:\
user-table=users:digestrealm-password-enc-algorithm=AES:digest-algorithm=SHA-256:encoding=Hex:charset=UTF-8 JDBCRealm

echo "* Installion ActiveMQ resource *"
sudo wget https://repo1.maven.org/maven2/org/apache/activemq/activemq-rar/5.16.1/activemq-rar-5.16.1.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin deploy --type rar --name activemq-rar ./activemq-rar-5.16.1.rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-resource-adapter-config --threadpoolid thread-pool-1 --property ServerUrl=\"tcp\\://localhost\\:61616\":UserName=admin:Password=$ADMIN_PWD activemq-rar
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-connector-connection-pool --raname activemq-rar --connectiondefinition javax.jms.ConnectionFactory --ping true jms/activeMQConnectionFactory-Connection-Pool
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin create-connector-resource --poolname jms/activeMQConnectionFactory-Connection-Pool --enabled true jms/activeMQConnectionFactory

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  create-system-properties AmqUrl="tcp\\://localhost\\:61616"
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  create-system-properties AmqUser=admin
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  create-system-properties AmqPwd=$ADMIN_PWD


sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  delete-jvm-options '-Xms512m'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  delete-jvm-options '-Xmx512m'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  create-jvm-options '-Xms2g'
sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin  create-jvm-options '-Xmx2g'

echo "* Installion Alertflex applications *"  
cd $INSTALL_PATH

sudo $GLASSFISH_PATH/bin/asadmin --passwordfile password.txt --user admin deploy target/alertflex-ctrl.war

sudo /etc/init.d/payara_domain1 restart

rm password.txt
