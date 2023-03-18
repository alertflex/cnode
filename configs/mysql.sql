-- do not change any settings

ALTER USER root@'localhost' IDENTIFIED WITH mysql_native_password BY '_db_pwd';

CREATE USER _db_user@'_db_host' IDENTIFIED WITH mysql_native_password BY '_db_pwd';

CREATE USER misp@'localhost' IDENTIFIED WITH mysql_native_password BY '_db_pwd';

CREATE DATABASE alertflex;
GRANT ALL ON alertflex.* TO _db_user@'_db_host';

CREATE DATABASE afevents;
GRANT ALL ON afevents.* TO _db_user@'_db_host';

CREATE DATABASE misp;
GRANT ALL ON misp.* TO misp@'localhost';

FLUSH PRIVILEGES;
