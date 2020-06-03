# Alertflex Cnode

**Alertflex Cnode** is a part of the Alertflex project. For more information, please see [https://alertflex.org](https://alertflex.org)


## Installation instructions

* Requirements:
Alertflex controller was tested under Ubuntu version 16.04 and Centos 7
	
* Download installation files

```
  git clone git://github.com/olegzhr/controller.git
  cd controller
```

* Fill in project specific parameters in file ``env.sh``

* Only for Centos 7

```
  sudo yum -y install epel-release
  sudo yum -y update
```

* Start installation
	
```
  chmod u+x install_ubuntu16.sh
  ./install_ubuntu16.sh
```
	
## Support

Please [open an issue on GitHub](https://github.com/olegzhr/controller/issues), if you'd like to report a bug or request a feature. 
Have a question or need tech support, please send an email to address: info@alertflex.org
