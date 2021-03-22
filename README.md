The repository includes Alertflex controller source code, a common installation script and configs files for Alertflex central node, the MISP docker install script.

Alertflex works as a security event manager and implements CTI, EDR, NTA and SOAR technology stack based on integration with third-party cybersecurity products (more than 20 are integrated at the moment).

![](https://github.com/alertflex/cnode/blob/master/img/dashboard.png)

The Alertflex implements five levels of security event management technology: Collection, Streaming, Analysis, Storage, Access. 
Additionally, Alertflex works as a security orchestrator for several third-party applications and automates security operations via Playbook.
For working inside a distributed environment of Hybrid IT, the solution consists of separate software components Collector, Controller, Management Console. 
Alertflex Controller and Management Console build up the Central node. The Central node could be placed inside of monitored IT infrastructure or outside. 
To exchange messages between the Remote node and the Central node, the ActiveMQ message broker is used. 
The security of connections between nodes is implemented on the basis of support for SSL / TLS protocols built into ActiveMQ. 
The solution can be easily scaled from the stand-alone appliance configuration to the distributed configuration for multi-clouds.

![](https://github.com/alertflex/cnode/blob/master/img/lld-arch.png)

For more information, please see the [Alertflex project documentation](https://alertflex.org/doc/index.html)
	
Please [open an issue on GitHub](https://github.com/alertflex/altprobe/issues), if you'd like to report a bug or request a feature. 
Have a question or need tech support, please send an email to address: info@alertflex.org
and join the community via [Alertflex Discord server](https://discord.gg/wDSz7rDMWv)

