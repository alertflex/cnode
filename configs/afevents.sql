CREATE TABLE `alert` (
  `alert_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `alert_uuid` char(37) NOT NULL DEFAULT '',
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `probe_name` varchar(512) NOT NULL DEFAULT '',
  `sensors_group` varchar(255) NOT NULL DEFAULT '',
  `alert_severity` varchar(64) NOT NULL DEFAULT '', -- OCSF severity: Critical | High | Medium | Low | Info
  `alert_source` varchar(32) NOT NULL DEFAULT '', -- falco suricata nuclei zap
  `alert_rule` varchar(512) NOT NULL DEFAULT '', -- falco.rule, suricata.signature_id, sarif.ruleId, 
  `alert_message` varchar(1024) NOT NULL DEFAULT '', -- falco.output, suricata.signature, sarif.message
  `src_ip` varchar(128) NOT NULL DEFAULT '',
  `dst_ip` varchar(128) NOT NULL DEFAULT '', -- zap.@host, nuclei.host
  `src_port` int(10) unsigned NOT NULL DEFAULT '0',
  `dst_port` int(10) unsigned NOT NULL DEFAULT '0', -- zap.@port, nuclei.port
  `user_name` varchar(512) NOT NULL DEFAULT '',
  `file_name` varchar(1024) NOT NULL DEFAULT '',
  `process_id` int(10) DEFAULT NULL,
  `process_name` varchar(512) NOT NULL DEFAULT '',
  `container_id` varchar(512) NOT NULL DEFAULT '',
  `container_name` varchar(512) NOT NULL DEFAULT '',
  `container_image` varchar(512) NOT NULL DEFAULT '',
  `pod_id` varchar(512) NOT NULL DEFAULT '',
  `pod_name` varchar(512) NOT NULL DEFAULT '',
  `name_space` varchar(512) NOT NULL DEFAULT '',
  `http_hostname` varchar(1024) NOT NULL DEFAULT '', -- 192.168.1.20
  `http_port` int(10) unsigned NOT NULL DEFAULT '0', -- 8080
  `http_url` varchar(1024) NOT NULL DEFAULT '', -- /alertflex-mc/api/v1/pipeline/status
  `http_method` varchar(512) NOT NULL DEFAULT '', --  POST
  `http_status` int(10) unsigned NOT NULL DEFAULT '0', -- 404
  `http_flow_id` varchar(512) NOT NULL DEFAULT '', -- 127638315028721
  `http_content_type` varchar(512) NOT NULL DEFAULT '', -- text/html
  `http_request_headers` varchar(2048) NOT NULL DEFAULT '', -- [{"name":"Authorization","value":"Bearer 4e08
  `http_request_body` varchar(2048) NOT NULL DEFAULT '', -- {\"username\":\"xyz\",\"password\":\"xyz\"}
  `pipeline_name` varchar(512) NOT NULL DEFAULT '',
  `pipeline_task` varchar(512) NOT NULL DEFAULT '',
  `job_name` varchar(512) NOT NULL DEFAULT '',
  `asset_name` varchar(512) NOT NULL DEFAULT '', -- name of asset
  `asset_target` varchar(512) NOT NULL DEFAULT '', -- nuclei: host name or ip, zap: url_address, pytest
  `test_uuid` char(37) NOT NULL DEFAULT '',
  `status` varchar(32) NOT NULL DEFAULT '', -- alert -> issue (issue can not be delete automatically)
  `original_time` varchar(512) NOT NULL DEFAULT '',
  `logged_time` datetime DEFAULT NULL,
  PRIMARY KEY (`alert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10180 DEFAULT CHARSET=utf8;

-- change remove test_type
CREATE TABLE `test` ( 
  `test_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `test_uuid` char(37) NOT NULL DEFAULT '',
  `project_id` varchar(255) NOT NULL DEFAULT '',
  `probe_name` varchar(512) NOT NULL DEFAULT '',
  `rule_id` varchar(512) NOT NULL DEFAULT '', -- sarif.ruleId
  `severity` varchar(255) NOT NULL DEFAULT '', -- sarif.level: error | warning | note | none
  `message` varchar(1024) NOT NULL DEFAULT '', -- sarif.message
  `location_message` varchar(1024) NOT NULL DEFAULT '', -- it is also can be snippet
  `location_uri` varchar(1024) NOT NULL DEFAULT '', -- sarif.locations_uri (owasp zap), sarif.locations_message (nuclei_network)
  `location_startline` int(10) unsigned NOT NULL DEFAULT '0',
  `location_startcolumn` int(10) unsigned NOT NULL DEFAULT '0',
  `location_endline` int(10) unsigned NOT NULL DEFAULT '0',
  `location_endcolumn` int(10) unsigned NOT NULL DEFAULT '0',  
  `pipeline_name` varchar(512) NOT NULL DEFAULT '',
  `pipeline_task` varchar(512) NOT NULL DEFAULT '',
  `job_name` varchar(512) NOT NULL DEFAULT '',
  `asset_name` varchar(512) NOT NULL DEFAULT '', -- name of asset
  `asset_target` varchar(512) NOT NULL DEFAULT '', -- nuclei: host name or ip, zap: url_address, pytest
  `status` varchar(32) NOT NULL DEFAULT '', -- unverified -> unverified
  `first_seen_time` datetime DEFAULT NULL,
  `last_seen_time` datetime DEFAULT NULL,
  `test_format` varchar(255) NOT NULL DEFAULT '',
  `test_text` text,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10180 DEFAULT CHARSET=utf8;


