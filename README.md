The repository contains the source code for Alertflex Controller, an installation script, and configuration files for the Central node.

The Alertflex solution works as a Security Event Manager and DevSecOps Orchestrator for a distributed grid of security sensors and scanners in a Hybrid IT infrastructure, which includes both on-premises and cloud-based systems. Solution supports the full DevSecOps cycle. It is capable of performing vulnerability/misconfiguration scanning tasks (DAST, SAST, SCA), but also excels in continuous monitoring by aggregating and forwarding cybersecurity events from sensors (HIDS, NIDS, WAF) to Log and Incident Management systems.

Alertflex can monitor different platforms such as Windows, Linux, Docker, K8s, and AWS, and offers a unified interface for over cybersecurity 20 tools. These integrated tools are primarily free open-source software, which Alertflex consolidates into one or multiple projects.

For more information see [Alertflex documentation](https://alertflex.github.io/doc) and [Test cases](https://github.com/alertflex/cnode/blob/master/test-cases.md)

Main benefits of using Alertflex project:
- seamless connectivity between central node and remote nodes, without requiring the opening of ports on the remote nodes
- the central node serves as a single point for running and collecting scans across the entire hybrid cloud infrastructure
- each remote node can have its own unique set of policies to filter alerts from sensors and reduce the impact of false positives
- various methods available for correlating alerts related to vulnerability and misconfiguration findings
- direct connection to the CTI MISP database to enable fast processing and caching of IoCs for a large number of security events

The high-level design:
![](https://github.com/alertflex/cnode/blob/master/img/arch.png)

List of supported sensors:
- AWS Network Firewall
- AWS WAF
- Falco (Cloud Native Runtime Security)
- ModSecurity (WAF)
- Suricata (NIDS)
- Wazuh (HIDS, EDR)

List of supported scanners:
- Trivy (SCA)
	- AppSecret
	- DockerConfig
	- K8sConfig
	- AppVuln
	- DockerVuln
	- K8sVuln
	- AppSbom
	- DockerSbom
- Kube-hunter (SCA, CSPM) 
- CloudSploit (Cloud Security Posture Management)
- Nikto (DAST)
- Nmap (DAST)
- Nuclei (DAST)
- Zap (DAST)
- Semgrep (SAST)
- SonarQube (SAST)

Please [open an issue on GitHub](https://github.com/alertflex/cnode/issues), if you'd like to report a bug or request a feature.
Have a question or need tech support, please send an email to address: info@alertflex.org
