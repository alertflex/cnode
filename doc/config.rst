Filtering policies
------------------

Filtering policies are files in JSON format, that describer operation such as alert aggregation and modification, whitelist and blacklist for alerts. Alertflex collector (Altprobe) applies the policy for every new event that comes to the system. Below, example part of filtering policy for Wazuh alerts:

.. parsed-literal::
	"hids": {
        "log": true,
        "severity": {
            "threshold": 1,
            "level0": 2,
            "level1": 4,
            "level2": 10
        },
        "gray_list": [{ 
            "event": "5715",
            "agent": "flghost",
            "match": "indef",
            "aggregate": {
                "reproduced": 0,
                "in_period": 0
            },
            "response": {
                "profile": "indef",
                "new_type": "indef",
                "new_source": "indef",
                "new_event": "55715",
                "new_severity": 1,
                "new_category": "new cat",
                "new_description": "new desc"
            }
        }]
    }
	
In the example above, Graylist consists policy for the event with ID 5715 (Wazuh classification), if an event with such ID come to Altprobe, next actions apply for events:

* ID of the event will be modified to 55715
* level of severity for the alert will be 1
* new category **new cat** will be added to alert
* description of alert will be modified to **new desc**
	
The filtering policy is loaded during the start of the Altprobe from file ``filter.json``, that located in the directory ``/etc/altprobe``. Also, the filtering policy can be dynamically changed and loaded to collectors from the central node via the Alertflex console. On the central node different versions of filtering policies store in dedicated folders for every collector node. For operations with filtering policy via Alertflex console open web-form **IDS/filter policies**

.. image:: /images/ids-filters.png

|

Central management of IDS configs
---------------------------------

For operations with IDS configs via Alertflex console, open web-form **IDS/config**

.. image:: /images/ids-config.png

|

Central management of IDS rules
---------------------------------

For operations with IDS rules via Alertflex console, open web-form **IDS/config**

.. image:: /images/ids-rules.png

|

Troubleshooting
---------------

* Open ActiveMQ console (default user ``admin``, password was set in ``env.sh`` file). Check messages in **Queues** panel, the amount of **Messages Enqueued** should be equal to the amount **Messages Dequeued**.

.. image:: /images/activemq.png

* Reload Alertflex applications if required

.. image:: /images/payara-reload.png

* How to check an Altprobe errors:

.. parsed-literal::
	cat /var/log/syslog | grep altprobe

