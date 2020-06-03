-- do not change any settings

CREATE USER misp@'localhost' IDENTIFIED BY '_mispdb_pwd';
CREATE USER misp@'192.168.200.2' IDENTIFIED BY '_mispdb_pwd';

CREATE DATABASE misp;
GRANT ALL ON misp.* TO misp@'localhost' IDENTIFIED BY '_mispdb_pwd';
GRANT ALL ON misp.* TO misp@'192.168.200.2' IDENTIFIED BY '_mispdb_pwd';
FLUSH PRIVILEGES;
