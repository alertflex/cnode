Installation of Cnode
---------------------

* Requirements:

Cnode was tested under Ubuntu version 16.04, Centos 7 and Amazon Linux 2.
Minimum Memory on a computer/virtual machine not less than 4GB, Hard disk not less 20GB.
The cnode should be reachable for remote components of the solution via hostname (see file /etc/hosts or DNS service).
Next TCP/UDP ports should be open for interaction between solution components and users. 

+----------+-------------+----------+
| port     | application | type     |
+----------+-------------+----------+
| 22       |  SSH        | admin    |
+----------+-------------+----------+
| 443      |  SSL        | web      |
+----------+-------------+----------+
| 4848     |  Payara AS  | admin ui |
+----------+-------------+----------+
| 8443     |  MISP       | admin ui |
+----------+-------------+----------+
| 61617    |  ActiveMQ   | altprobe |
+----------+-------------+----------+

	
* Download installation files

.. parsed-literal::
  git clone git://github.com/olegzhr/cnode.git
  cd cnode

* Fill in project-specific parameters in file ``env.sh``, as an example see file ``example_env.sh``

* This step only for Centos 7 and Amazon Linux.

.. parsed-literal::
  sudo yum -y install epel-release
  sudo yum -y update

* Start installation
	
.. parsed-literal::
  chmod u+x install_ubuntu16.sh
  ./install_ubuntu16.sh

* After finished the installation, reboot system

Post-installation
-----------------

* Open the landing page of the Cnode by typing hostname/IP address in a browser. Via a menu of the page, you can get access to Alertflex web console or admin consoles of ActiveMQ, MISP, Payara AS. For login to Alertflex console use default user ``admin``, password was set in ``env.sh`` file.
.. image:: /images/main-page.png

* In Alertflex console select "Settings" and download SSL certificate for Alertlex collector (altprobe).

.. image:: /images/project.png

* Check parameter Project-ref, it also will be required during the installation of Alertflex collector.

.. image:: /images/project-id.png

* For enabling of MISP feeds, login to MISP admin console  (default user ``admin@admin.test``, password ``admin``).

.. image:: /images/misp-feeds.png

* Now, Cnode should be ready for service and you can start the installation of collectors (altprobe)

