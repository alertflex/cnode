-- do not change any settings

CREATE USER grafana@'localhost' IDENTIFIED BY '_db_pwd';

CREATE DATABASE grafana;
GRANT ALL ON grafana.* TO grafana@localhost IDENTIFIED BY '_db_pwd';
FLUSH PRIVILEGES;

