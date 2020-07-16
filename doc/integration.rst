Open Cybersecurity Alliance ecosystem (STIX-shifter)
----------------------------------------------------

Alertflex has an adapter for STIX-shifter to interact with Alertflex controller via REST API. It translates STIX pattern to pseudo SQL requests and transmits to the controller to get information about IDS alerts in STIX objects format.

Below several examples using Alertflex stix-shifter adapter:

* Check availability of Alertflex REST interface (return code: ``HTTP/1.1 200 OK``)

.. parsed-literal:: 
	curl -k -v GET -u admin:XXXXXXX https://192.168.1.10:8181/alertflex-ctrl/rest/stix-alerts/status

* Ping service from STIX-shifter

.. parsed-literal:: 
	python main.py transmit alertflex
	'{"host":"192.168.1.10", "port":"8181"}'
	'{"auth": {"username": "admin","password": "XXXXXXXX"}}'
	ping

* Get info about alerts where a certain file's md5 hash is presented

.. parsed-literal:: 
	python main.py execute alertflex alertflex
	'{"type": "identity", "id": "identity--3532c56d-ea72-48be-a2ad-1a53f4c9c6d3", "name": "Alertflex", "identity_class": "events"}'
	'{"host":"192.168.1.10", "port":"8181"}' '{"auth": {"username": "admin","password": "XXXXXXX"}}'
	"[file:hashes.MD5 = '02d2a1d8b353ba2bf59ca381f1836ebd']"

* Get info about alerts with type "HOST" raised during a fixed interval of time

.. parsed-literal:: 
	python main.py execute alertflex alertflex
	'{"type": "identity", "id": "identity--3532c56d-ea72-48be-a2ad-1a53f4c9c6d3", "name": "Alertflex", "identity_class": "events"}'
	'{"host":"192.168.1.10", "port":"8181"}' '{"auth": {"username": "admin","password": "XXXXXXX"}}'
	"[x_org_alertflex:type = 'HOST'] START t'2020-06-09T00:00:00Z' STOP t'2020-06-09T20:11:11Z'"

|

Send alerts and Netflow to Graylog
----------------------------------

* In Alertflex console select "Settings/Log server" and add IP/host address and port of Graylog server. Select checkbox ``send Netflow`` if you want send Netflow records from Suricata

.. image:: /images/graylog-config.png

* In Graylog console select "System/Inputs" and add ``GELF UDP`` input

.. image:: /images/graylog-input.png

|

Send alerts and Netflow to ElasticStack/Open Distro via Logstash
----------------------------------------------------------------

* Configure LogStash receive events from Alertflex via ``GELF UDP``:

.. parsed-literal:: 
	input {
		gelf {
			host => "0.0.0.0"
			type => "alertflex"
		}
	}

	output {
		elasticsearch {
			hosts => ["https://localhost:9200"]
			index => "logstash"
			user => "logstash"
			password => "logstash"
			ssl_certificate_verification => false
			ilm_enabled => false
		}
	}
	
|

Enable receive metrics from Alertflex in Prometheus/Grafana
-----------------------------------------------------------

* In Alertflex console open "Settings/Parameters" web-form and select checkbox ``Enable REST interface for metrics``

.. image:: /images/prometheus-config.png

* Configure Prometheus to do requests of metrics from Alertflex, edit the file ``/etc/prometheus/prometheus.yml``

.. parsed-literal:: 
	- job_name: 'alertflex'
		metrics_path: /alertflex-ctrl/rest/metrics
		static_configs:
		- targets: ['192.168.1.10:8080']

	}

|
