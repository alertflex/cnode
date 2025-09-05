CREATE USER admin@'localhost' IDENTIFIED WITH mysql_native_password BY '_admin_pwd';

CREATE DATABASE alertflex;
GRANT ALL ON alertflex.* TO admin@'localhost';

CREATE DATABASE afevents;
GRANT ALL ON afevents.* TO admin@'localhost';

FLUSH PRIVILEGES;


