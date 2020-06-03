-- do not change any settings

CREATE USER misp@'localhost' IDENTIFIED BY '_mispdb_pwd';

CREATE DATABASE misp;
GRANT ALL ON misp.* TO misp@'localhost' IDENTIFIED BY '_mispdb_pwd';
FLUSH PRIVILEGES;

USE misp;

CREATE TABLE `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `info` text COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL,
  `uuid` varchar(40) COLLATE utf8_bin NOT NULL,
  `published` tinyint(1) NOT NULL DEFAULT 0,
  `analysis` tinyint(4) NOT NULL,
  `attribute_count` int(11) unsigned DEFAULT 0,
  `orgc_id` int(11) NOT NULL,
  `timestamp` int(11) NOT NULL DEFAULT 0,
  `distribution` tinyint(4) NOT NULL DEFAULT 0,
  `sharing_group_id` int(11) NOT NULL,
  `proposal_email_lock` tinyint(1) NOT NULL DEFAULT 0,
  `locked` tinyint(1) NOT NULL DEFAULT 0,
  `threat_level_id` int(11) NOT NULL,
  `publish_timestamp` int(11) NOT NULL DEFAULT 0,
  `sighting_timestamp` int(11) NOT NULL DEFAULT 0,
  `disable_correlation` tinyint(1) NOT NULL DEFAULT 0,
  `extends_uuid` varchar(40) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uuid` (`uuid`),
  INDEX `info` (`info`(255)),
  INDEX `sharing_group_id` (`sharing_group_id`),
  INDEX `org_id` (`org_id`),
  INDEX `orgc_id` (`orgc_id`),
  INDEX `extends_uuid` (`extends_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL DEFAULT 0,
  `object_relation` varchar(255) COLLATE utf8_bin,
  `category` varchar(255) COLLATE utf8_bin NOT NULL,
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `value1` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `value2` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `to_ids` tinyint(1) NOT NULL DEFAULT 1,
  `uuid` varchar(40) COLLATE utf8_bin NOT NULL,
  `timestamp` int(11) NOT NULL DEFAULT 0,
  `distribution` tinyint(4) NOT NULL DEFAULT 0,
  `sharing_group_id` int(11) NOT NULL,
  `comment` text COLLATE utf8_bin,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `disable_correlation` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `event_id` (`event_id`),
  INDEX `object_id` (`object_id`),
  INDEX `object_relation` (`object_relation`),
  INDEX `value1` (`value1`(255)),
  INDEX `value2` (`value2`(255)),
  INDEX `type` (`type`),
  INDEX `category` (`category`),
  INDEX `sharing_group_id` (`sharing_group_id`),
  UNIQUE INDEX `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
