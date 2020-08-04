# Alertflex central node

Alertflex project is a continuous security monitoring solution designed for use in Hybrid Clouds. By monitoring events near real-time from well-known open-source security applications, Alertflex provides end-to-end security visibility for on-premises and cloud-based IT infrastructure. 

Alertflex implements two main functionality:

**Security event management** for a distributed hub of security sensors (Suricata NIDS, Wazuh HIDS, Falco CRS, Modsecurity WAF) based on the next levels: 
* Collection (Alertflex collector)
* Streaming (ActiveMQ)
* Analysis  (Alertflex controller)
* Storage (MySQL)
* Access  (Alertflex controller and console)

**Security operations automation and orchestration**
* IDS centralized management for rules, configs, filtering policies, IP address blocking lists
* CTI functions which are based on integration with MISP. Performs a reputation checks for IP addresses, DNS records, MD5, SHA1 SHA256 hashes of files. Creates an alert, in case of suspicious data has been found.
* Can redirect alerts, Netflow, logs  to open-source Log Management and monitoring systems ( Graylog, ElasticStack, Prometheus/Grafana)
* Can periodically to run scanning of remote files in Malware Analysis Sandbox (Cuckoo, Hybrid Analysis, VMRay)
* Integrates with SAST and DAST tools (Nmap, SonarQube, OWASP ZAP)
* Provides REST API for IDS alerts, compatible with Open Cybersecurity Alliance ecosystem

**Central node** includes Alertflex controller and console, ActiveMQ server, MISP docker container. Additionally can be installed Grafana, Nmap, OWASP ZAP 

## Cnode documentation:

* [installation](https://github.com/olegzhr/cnode/blob/master/doc/install.rst)

* [configuration](https://github.com/olegzhr/cnode/blob/master/doc/config.rst)

* [integration](https://github.com/olegzhr/cnode/blob/master/doc/integration.rst)

For more information, please see the [Alertflex project presentation](https://github.com/alertflex/cnode/blob/master/doc/alertflex.pdf)
	
## Support

Please [open an issue on GitHub](https://github.com/olegzhr/cnode/issues), if you'd like to report a bug or request a feature. 
Have a question or need tech support, please send an email to address: info@alertflex.org
