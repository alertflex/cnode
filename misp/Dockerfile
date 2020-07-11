FROM ubuntu:18.04

# 80/443 - MISP web server, 50000 - MISP ZeroMQ
EXPOSE 80 443 50000

# User supplied inputs
ARG MYSQL_MISP_HOST=ChangeThisDefaultHost9564ebc3289b7a14551baf8ad5ec60a
ARG MYSQL_MISP_PASSWORD=ChangeThisDefaultPassworda9564ebc3289b7a14551baf8ad5ec60a
ARG POSTFIX_RELAY_HOST=localhost
ARG MISP_FQDN=localhost
ARG MISP_EMAIL=admin@localhost
ARG MISP_GPG_PASSWORD=ChangeThisDefaultPasswordXuJBao5Q2bps89LWFqWkKgDZwAFpNHvc

ENV DEBIAN_FRONTEND noninteractive
ENV DEBIAN_PRIORITY critical
RUN apt-get update && apt-get install -y supervisor cron logrotate syslog-ng-core postfix curl gcc git gnupg-agent make python3 openssl redis-server sudo vim zip wget mariadb-client sqlite3 moreutils apache2 apache2-doc apache2-utils libapache2-mod-php php php-cli php-gnupg php-dev php-json php-mysql php7.2-opcache php-readline php-redis php-xml php-mbstring rng-tools python3-dev python3-pip python3-yara python3-redis python3-zmq libxml2-dev libxslt1-dev zlib1g-dev python3-setuptools libpq5 libjpeg-dev libfuzzy-dev ruby asciidoctor tesseract-ocr imagemagick libpoppler-cpp-dev

# Edit the php.ini file to adjust initial PHP settings to MISP recommended settings
RUN sed -i "s/max_execution_time = 30/max_execution_time = 300/" /etc/php/7.2/apache2/php.ini ; \
    sed -i "s/memory_limit = 128M/memory_limit = 2048M/" /etc/php/7.2/apache2/php.ini ; \
    sed -i "s/upload_max_filesize = 2M/upload_max_filesize = 50M/" /etc/php/7.2/apache2/php.ini ; \
    sed -i "s/post_max_size = 8M/post_max_size = 50M/" /etc/php/7.2/apache2/php.ini

RUN sed -i -E 's/^(\s*)system\(\);/\1unix-stream("\/dev\/log");/' /etc/syslog-ng/syslog-ng.conf ; \
    sed -i "s/daemonize yes/daemonize no/" /etc/redis/redis.conf ; \
    a2dismod status ; \
    a2enmod ssl rewrite headers; \
    a2ensite 000-default ; \
    a2ensite default-ssl ; \
    mkdir -p /var/www/MISP /root/.config /root/.git


WORKDIR /var/www/MISP
RUN chown -R www-data:www-data /var/www/MISP /root/.config /root/.git; \
    sudo -u www-data -H git clone https://github.com/MISP/MISP.git /var/www/MISP ; \
    sudo -u www-data -H git submodule update --init --recursive ; \
    sudo -u www-data -H git submodule foreach --recursive git config core.filemode false ; \
    sudo -u www-data -H git config core.filemode false ; \
    echo

RUN sudo pip3 install --upgrade pip ; \
    sudo pip3 install git+https://github.com/CybOXProject/mixbox.git ; \
    sudo pip3 install git+https://github.com/CybOXProject/python-cybox.git ; \
    sudo pip3 install git+https://github.com/STIXProject/python-stix.git ; \
    sudo pip3 install git+https://github.com/MAECProject/python-maec.git ; \
    sudo pip3 install /var/www/MISP/cti-python-stix2 ; \
    sudo pip3 install /var/www/MISP/PyMISP ; \
    sudo pip3 install git+https://github.com/kbandla/pydeep.git ; \
    sudo pip3 install https://github.com/lief-project/packages/raw/lief-master-latest/pylief-0.9.0.dev.zip ; \
    sudo pip3 install jsonschema ; \
    sudo pip3 install reportlab ; \
    sudo pip3 install python-magic ; \
    sudo pip3 install pyzmq ; \
    sudo pip3 install redis

WORKDIR /var/www/MISP
RUN sudo -u www-data -H git submodule init ; \
    sudo -u www-data -H git submodule update

WORKDIR /usr/local/src
RUN sudo -H git clone https://github.com/MISP/misp-modules.git

WORKDIR /usr/local/src/misp-modules
RUN sudo -H git checkout -b v2.4.104 ; \
    sudo pip3 install -I -r REQUIREMENTS ;  \
    sudo pip3 install -I .

WORKDIR /var/www/MISP/app
RUN mkdir /var/www/.composer && chown -R www-data:www-data /var/www/.composer ; \
    sudo -u www-data -H wget https://getcomposer.org/download/1.2.1/composer.phar -O composer.phar ; \
    sudo -u www-data -H php composer.phar require kamisama/cake-resque:4.1.2 ; \
    sudo -u www-data -H php composer.phar config vendor-dir Vendor ; \
    sudo -u www-data -H php composer.phar install ; \
    sudo phpenmod redis ; \
    sudo -u www-data -H cp -fa /var/www/MISP/INSTALL/setup/config.php /var/www/MISP/app/Plugin/CakeResque/Config/config.php ; \
    sudo chown -R www-data:www-data /var/www/MISP ; \
    sudo chmod -R 750 /var/www/MISP ; \
    sudo chmod -R g+ws /var/www/MISP/app/tmp ; \
    sudo chmod -R g+ws /var/www/MISP/app/files ; \
    sudo chmod -R g+ws /var/www/MISP/app/files/scripts/tmp ; \
    sudo -u www-data cp -a /var/www/MISP/app/Config/bootstrap.default.php /var/www/MISP/app/Config/bootstrap.php ; \
    sudo -u www-data cp -a /var/www/MISP/app/Config/database.default.php /var/www/MISP/app/Config/database.php ; \
    sudo -u www-data cp -a /var/www/MISP/app/Config/core.default.php /var/www/MISP/app/Config/core.php ; \
    sudo -u www-data cp -a /var/www/MISP/app/Config/config.default.php /var/www/MISP/app/Config/config.php

RUN sed -i -e "s/bind 127.0.0.1 ::1/bind 0.0.0.0/" /etc/redis/redis.conf ; \
    sudo chown -R www-data:www-data /var/www/MISP/app/Config ; \
    sudo chmod -R 750 /var/www/MISP/app/Config ; \
	sudo -u www-data -H wget https://github.com/ssdeep-project/ssdeep/releases/download/release-2.14.1/ssdeep-2.14.1.tar.gz ; \
    tar zxvf ssdeep-2.14.1.tar.gz && cd ssdeep-2.14.1 && ./configure && make && sudo make install ; \
    sudo pecl install ssdeep ; \
    sudo echo "extension=ssdeep.so" > /etc/php/7.2/mods-available/ssdeep.ini ; \
    sudo phpenmod ssdeep ;

WORKDIR /etc/logrotate.d
RUN echo "/var/www/MISP/app/tmp/logs/resque-*-error.log {" > misp ; \
    echo "      rotate 30" >> misp ; \
    echo "      dateext" >> misp ; \
    echo "      missingok" >> misp ; \
    echo "      notifempty" >> misp ; \
    echo "      compress" >> misp ; \
    echo "      weekly" >> misp ; \
    echo "      copytruncate" >> misp ; \
    echo "}" >> misp ; \
    chmod 0640 /etc/logrotate.d/misp

WORKDIR /var/www/MISP/app
RUN postconf -e "relayhost = $POSTFIX_RELAY_HOST" ; \
	echo "<VirtualHost *:80>" > /etc/apache2/sites-available/000-default.conf ; \
    echo "ServerName $MISP_FQDN" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "Redirect permanent / https://$MISP_FQDN" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "LogLevel warn" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "ErrorLog /var/log/apache2/misp_error.log" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "CustomLog /var/log/apache2/misp_access.log combined" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "ServerSignature Off" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "</VirtualHost>" >> /etc/apache2/sites-available/000-default.conf ; \
    echo "<VirtualHost *:443>" > /etc/apache2/sites-available/default-ssl.conf ; \
    echo "ServerAdmin $MISP_EMAIL" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "ServerName $MISP_FQDN" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "DocumentRoot /var/www/MISP/app/webroot" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "<Directory /var/www/MISP/app/webroot>" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "Options -Indexes" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "AllowOverride all" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "</Directory>" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "SSLEngine On" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "SSLCertificateFile /etc/ssl/private/misp.crt" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "SSLCertificateKeyFile /etc/ssl/private/misp.key" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "#SSLCertificateChainFile /etc/ssl/private/misp-chain.crt" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "LogLevel warn" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "ErrorLog /var/log/apache2/misp_ssl_error.log" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "CustomLog /var/log/apache2/misp_ssl_access.log combined" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "ServerSignature Off" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "</VirtualHost>" >> /etc/apache2/sites-available/default-ssl.conf ; \
    echo "ServerName localhost" >> /etc/apache2/apache2.conf ;
	
COPY nginx.key /etc/ssl/private/misp.key
COPY nginx.crt /etc/ssl/private/misp.crt
    
RUN sed -i -e 's/db login/misp/g' /var/www/MISP/app/Config/database.php ; \
	sed -i -e "s/db password/${MYSQL_MISP_PASSWORD}/g" /var/www/MISP/app/Config/database.php ; \
	sed -i -e "s/localhost/${MYSQL_MISP_HOST}/g" /var/www/MISP/app/Config/database.php ; \
	sed -i -E "s/'salt'(\s+)=>\s''/'salt' => '`openssl rand -base64 32 | tr \'/\' \'0\'`'/" /var/www/MISP/app/Config/config.php ; \
	sed -i -E "s/'baseurl'(\s+)=>\s''/'baseurl' => 'https:\/\/${MISP_FQDN}'/" /var/www/MISP/app/Config/config.php ; \
	sed -i -e "s/email@address.com/${MISP_EMAIL}/" /var/www/MISP/app/Config/config.php
	
COPY db-init.sh /db-init.sh
RUN chmod 755 /db-init.sh
	
RUN sudo -u www-data -H mkdir /var/www/MISP/.gnupg ; \
    chmod 700 /var/www/MISP/.gnupg ; \
    echo "Key-Type: 1" > /tmp/config_gpg ; \
    echo "Key-Length: 4096" >> /tmp/config_gpg ; \
    echo "Subkey-Type: 1" >> /tmp/config_gpg ; \
    echo "Subkey-Length: 4096" >> /tmp/config_gpg ; \
    echo "Name-Real: MISP" >> /tmp/config_gpg ; \
    echo "Name-Email: $MISP_EMAIL" >> /tmp/config_gpg ; \
    echo "Expire-Date: 0" >> /tmp/config_gpg ; \
    echo "Passphrase: $MISP_GPG_PASSWORD" >> /tmp/config_gpg ; \
    chmod 700 /tmp/config_gpg ; \
    sudo rm -f /dev/random ; \
    sudo mknod -m 0666 /dev/random c 1 9 ; \
    sudo echo HRNGDEVICE=/dev/urandom | sudo tee -a /etc/default/rng-tools ; \
    sudo /etc/init.d/rng-tools restart ; \
    sudo rngd -f -r /dev/urandom ; \
    chown www-data /tmp/config_gpg ; \
    sudo -u www-data sh -c "gpg --batch --homedir /var/www/MISP/.gnupg --gen-key /tmp/config_gpg" ; \
    sudo -u www-data sh -c "gpg --homedir /var/www/MISP/.gnupg --export --armor $MISP_EMAIL > /var/www/MISP/app/webroot/gpg.asc" ; \
    sudo /etc/init.d/rng-tools stop ; \
    sudo apt-get remove --purge -y rng-tools

WORKDIR /var/www/MISP
COPY supervisord.conf /etc/supervisor/conf.d/
COPY bootstrap.css /var/www/MISP/app/webroot/css/

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
