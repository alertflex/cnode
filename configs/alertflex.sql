CREATE TABLE `project` (
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `project_name` varchar(255) NOT NULL DEFAULT '',
  `project_status` int(2) unsigned NOT NULL DEFAULT '0', -- 0 not_active, 1 active, 2 free_service, 3 pro_service
  `alerts_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `alerts_ttl` int(10) unsigned NOT NULL DEFAULT '0',
  `tasks_ttl` int(10) unsigned NOT NULL DEFAULT '0',
  `api_key` varchar(255) NOT NULL DEFAULT '',
  `api_auth` int(2) unsigned NOT NULL DEFAULT '0', -- 0 disable , 1 auth by key, 2 by pipeline id also
  `last_update` datetime DEFAULT NULL,
  `next_update` datetime DEFAULT NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO project VALUES ("_project_id", "default", 0, 1000, 0, 0, "", 1 , NOW(), NOW());

CREATE TABLE `users` (
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `userid` varchar(150) NOT NULL,
  `password` varchar(150) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `dashboard_timerange` int(10) unsigned NOT NULL DEFAULT '0',
  `color_theme` int(2) unsigned NOT NULL DEFAULT '0',
  `layout_menu` int(2) unsigned NOT NULL DEFAULT '0',
  `font_size` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `users` VALUES ("_project_id", "admin", SHA2("_admin_pwd", 256), "", 1, 1, 1, 14);

CREATE TABLE `groups` (
  `groupid` varchar(100) NOT NULL,
  `userid` varchar(150) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- INSERT INTO `groups` VALUES ("admin", "admin");
INSERT INTO `groups` VALUES ("admin", "admin");

CREATE TABLE `tools` (
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `service_url` varchar(512) NOT NULL DEFAULT '',
  `service_key` varchar(512) NOT NULL DEFAULT '',
  `opensearch_url` varchar(512) NOT NULL DEFAULT '',
  `opensearch_port` int(10) unsigned NOT NULL DEFAULT '0',
  `opensearch_user` varchar(512) NOT NULL DEFAULT '',
  `opensearch_pwd` varchar(512) NOT NULL DEFAULT '',
  `discord_hook` varchar(512) NOT NULL DEFAULT '',
  `telegram_chatid` varchar(128) NOT NULL DEFAULT '',
  `telegram_key` varchar(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `tools` VALUES ("_project_id","","","_os_url",_os_port,"_os_user","_os_pwd","","","");

CREATE TABLE `pipelines` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `pipeline_uuid` varchar(512) NOT NULL DEFAULT '',
  `pipeline_name` varchar(512) NOT NULL DEFAULT '',
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  `pipeline_text` text,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `quality_gates` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `qg_name` varchar(512) NOT NULL DEFAULT '',
  `test_critical` int(10) unsigned NOT NULL DEFAULT '0',
  `test_high` int(10) unsigned NOT NULL DEFAULT '0',
  `test_medium` int(10) unsigned NOT NULL DEFAULT '0',
  `test_low` int(10) unsigned NOT NULL DEFAULT '0',
  `monitor_critical` int(10) unsigned NOT NULL DEFAULT '0',
  `monitor_high` int(10) unsigned NOT NULL DEFAULT '0',
  `monitor_medium` int(10) unsigned NOT NULL DEFAULT '0',
  `monitor_low` int(10) unsigned NOT NULL DEFAULT '0',
  `anomaly_detector_id` varchar(512) NOT NULL DEFAULT '',
  `anomaly_detector_name` varchar(512) NOT NULL DEFAULT '',
  `anomaly_detector_score` int(10) unsigned NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `artifacts` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `artifact_name` varchar(512) NOT NULL DEFAULT '',
  `artifact_type` varchar(64) NOT NULL DEFAULT '', -- PYTHON, YAML, JSON, SHELL
  `status` varchar(255) NOT NULL, -- manual , ai_based, waiting, error
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  `test_framework` varchar(64) NOT NULL DEFAULT '', -- python , postman
  `test_count` int(10) unsigned NOT NULL DEFAULT '10',
  `content_type` varchar(64) NOT NULL DEFAULT '', -- open_api , http_raw
  `content_data` varchar(4096) NOT NULL DEFAULT '', 
  `artifact_text` text,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO artifacts (
  `project_id`,
  `artifact_name`,
  `artifact_type`,
  `status`,
  `created_at`,
  `changed_at`,
  `test_framework`,
  `test_count`,
  `content_type`,
  `content_data`,
  `artifact_text`)
VALUES (
  '_project_id', 
  'plugin_pytest.sh', 'SHELL', 'manual', now(), now(), '', '0', '', '', 
  '#!/bin/bash\n\nWORKSPACE_PATH=$1\nTESTS_FILE=$2\nTESTS_FULL_PATH=$3\nOUTPUT_PATH=$4\nOUTPUT_FILE=$5\nOUTPUT_FULL_PATH=$6\nTARGET=$7\nRUN_MODE=$8\n\nset +e\npython3 -m pytest \"$TESTS_FULL_PATH\" --junitxml=\"$OUTPUT_FULL_PATH\"\nlogger \"script has been finished\"\nexit 0');

CREATE TABLE `task_pipelines` (
  `rec_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `pipeline_name` varchar(512) NOT NULL,
  `task_call` int(2) unsigned NOT NULL DEFAULT '0', -- 0 api | 1 cloud | 2 timer | 3 ui
  `task_uuid` varchar(255) NOT NULL,
  `task_status` varchar(255) NOT NULL, -- running | finished | aborted
  `qgs_status` varchar(255) NOT NULL, -- success | fail
  `error_job` varchar(512) NOT NULL DEFAULT '',
  `error_code` varchar(512) NOT NULL DEFAULT '',
  `started_time` datetime DEFAULT NULL,
  `stoped_time` datetime DEFAULT NULL,
  `report_format` varchar(255) NOT NULL, -- junit-xml | sarif
  `report_type` varchar(255) NOT NULL, -- short | full
  `report_text` text,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `task_jobs` (
  `rec_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `job_name` varchar(512) NOT NULL,
  `pipeline_name` varchar(512) NOT NULL,
  `pipeline_task` varchar(255) NOT NULL,
  `asset_name` varchar(512) NOT NULL DEFAULT '', -- name of asset
  `asset_target` varchar(512) NOT NULL DEFAULT '', -- nuclei: host name or ip, zap: url_address, pytest
  `probe_name` varchar(512) NOT NULL DEFAULT '',
  `task_status` varchar(255) NOT NULL, -- running | finished | aborted
  `error_code` varchar(512) NOT NULL DEFAULT '',
  `started_time` datetime DEFAULT NULL,
  `stoped_time` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `probes` (
   `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
   `project_id` varchar(255) NOT NULL DEFAULT '',
   `probe_name` varchar(512) NOT NULL DEFAULT '',
   `description` varchar(512) NOT NULL DEFAULT '',
   `sensors_group` varchar(255) NOT NULL DEFAULT '',
   `report_interval` int(10) unsigned NOT NULL DEFAULT '0',
   `enable_alerts` int(2) unsigned NOT NULL DEFAULT '0',
   `enable_logs` int(2) unsigned NOT NULL DEFAULT '0',
   `run_mode` varchar(512) NOT NULL,
   `path_workspace` varchar(512) NOT NULL,
   `path_output` varchar(512) NOT NULL,
   `app_log_status` int(2) unsigned NOT NULL DEFAULT '0',
   `app_redis_status` int(2) unsigned NOT NULL DEFAULT '0',
   `falco_log_status` int(2) unsigned NOT NULL DEFAULT '0',
   `falco_redis_status` int(2) unsigned NOT NULL DEFAULT '0',
   `suricata_log_status` int(2) unsigned NOT NULL DEFAULT '0',
   `suricata_redis_status` int(2) unsigned NOT NULL DEFAULT '0',
   `pipeline_name` varchar(512) NOT NULL DEFAULT '',
   `pipeline_task` varchar(255) NOT NULL DEFAULT '',
   `last_test_time` varchar(255) NOT NULL DEFAULT '',
   `first_status_time` datetime DEFAULT NULL,
   `last_status_time` datetime DEFAULT NULL,
   PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `variables` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `var_name` varchar(255) NOT NULL,
  `var_value` varchar(32) NOT NULL DEFAULT '',
  `description` varchar(512) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rules` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `rule_name` varchar(512) NOT NULL DEFAULT '',
  `rule_status` int(2) unsigned NOT NULL DEFAULT '1', -- off, on
  `pipeline_name` varchar(512) NOT NULL DEFAULT '',
  `alert_severity` varchar(64) NOT NULL DEFAULT '', -- OCSF severity: Critical | High | Medium | Low | Info
  `alert_source` varchar(32) NOT NULL DEFAULT '', -- falco suricata nuclei zap pytest
  `alert_rule` varchar(512) NOT NULL DEFAULT '', -- falco.rule, suricata.signature_id, sarif.ruleId, 
  `alert_message` varchar(1024) NOT NULL DEFAULT '', -- regex
  `dst_ip` varchar(128) NOT NULL DEFAULT '',
  `src_ip` varchar(128) NOT NULL DEFAULT '',
  `http_hostname` varchar(1024) NOT NULL DEFAULT '',
  `http_port` int(10) unsigned NOT NULL DEFAULT '0',
  `http_url` varchar(1024) NOT NULL DEFAULT '',
  `http_method` varchar(512) NOT NULL DEFAULT '',
  `http_request_body` varchar(2048) NOT NULL DEFAULT '', -- regex
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `assets` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `asset_name` varchar(255) NOT NULL,
  `description` varchar(512) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `targets` (
  `rec_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `target` varchar(512) NOT NULL,
  `target_type` varchar(255) NOT NULL,  -- ip | url | end_point | host_name | repo_path | docker_image
  `asset_name` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `changed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

