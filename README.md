The repository includes Alertflex Controller source code, installation script and config files for Central node, MISP docker install script.

Alertflex project is an automation, continuous monitoring, threat detection and response solution. Alertflex designed for use in Hybrid IT infrastructure (on-premises and cloud-based) and can monitor different types of platforms - Windows, Linux, Docker, Kubernetes, Amazon AWS.

The solution works as a Security Event Manager with SOAR functionality for a distributed grid of security sensors and scanners. At this moment Alertflex provides an orchestrator and a single pane of glass for more than 30 products. Integrated products are mostly free open-source software in the areas of Intrusion Detection and DevSecOps, which can be unified by Alertflex into one or multiple projects.

One of the advantages of Alertflex is cyber threat intelligence for security events and NetFlow in a mode close to real-time. The response to detected threats can be a running Playbook with predefined security operations. Integration Alertflex with the OpenDistro Anomaly Detection feature (Random Cut Forest AI algorithm), makes it possible to configure automated response not only for discrete events but for an anomaly rate of events as well.

![](https://github.com/alertflex/cnode/blob/master/img/dashboard.png)

For working inside of Hybrid IT environment, the solution consists of  distributed software components Collector, Controller, Management Console. Alertflex Controller and Management Console build up the Central node, which can be placed inside of monitored IT infrastructure or outside.

![](https://github.com/alertflex/cnode/blob/master/img/lld-arch.png)

For more information, please see [solution description](https://alertflex.github.io/solution.html) and [project documentation](https://alertflex.github.io/doc/index.html)
	
Please [open an issue on GitHub](https://github.com/alertflex/altprobe/issues), if you'd like to report a bug or request a feature. 
Have a question or need tech support, send an email to address: info@alertflex.org
and join the community via [Alertflex Discord server](https://discord.gg/wDSz7rDMWv)

