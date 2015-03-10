A java class for Service Now's External Credentials Plugin.  Allows for machine's UN/PWs for discovery to be stored locally at a client's Mid Server rather then in the cloud in Service Now.  Stores in local file that is encrypted with Windows DPAPI encryption.

Overview
--------

Sometimes clients would prefer not to store credentials for local networked machines in Service Now's instance for security reasons. These credentials are used by Discovery and Orchestration when the MID server launches certain probes that need access to a client's machines and have to log on through SSH, SNMP or Windows.

A ServiceNow instance can store credentials used by Discovery and Orchestration on an external credential repository rather than directly in a ServiceNow credentials record. The instance maintains a unique identifier for each credential, the credential type (such as SSH, SNMP, or Windows), and any credential affinities. The MID Server obtains the credential identifier from the instance, and then uses a customer-provided JAR file to resolve the identifier from the repository into a usable credential [More information available at this SN Wiki Article][]

I have developed a simple plugin (JAR file) which allows credential retrieval from a CSV file. The CSV file is encrypted and un-encrypted with Windows Data Protection ([(http://msdn.microsoft.com/en-us/library/ms995355.aspx MS Documentation]). This DPAPI (Data Protection API) uses a Triple-DES algorithm with a strong key to encrypt the data. It uses API calls available in Windows 2000 and later to accomplish this. The strong key used is the current user's password hash stored in windows. So, no password management or entry is needed. Since the JAR file is run by the MID server, the CSV file will be un-encrypted by the windows password hash of the service account the MID server is running as.

**Note**

-   This Java code can be modified to not use encryption if not needed by a client. The file can be locked down in Windows through ACL rules and by the assumption that the files on the internal server would not be compromised.
-   This integration needs a few more hours for testing and polishing out, as it was developed but never fully implemented implemented.

Benefits
------------------------

-   Provides high security clients ability store sensitive account credentials (used for Discovery and Orchestration) locally within their network instead of in SN's databases which are a hosted, offsite solution.
-   Sensitive account credentials for an organizations machines are stored securely in a Triple-DES encrypted CSV file, locally on your MID Server's file system.
-   Account credentials are loaded into memory and de-crypted only at run time when Discovery and Orchestration probes are sent to the MID server when work is needed.
-   Store SSH, SNMP, and Windows credentials.

Technical Diagrams
------------------

DPAPI Encryption. The JAR file would be the “Application” here:

![alt text](https://raw.githubusercontent.com/paulsena/SN-External-Credentials/master/Doc-Diagram-DPAPI.jpg "DPAI Diagram")

Instructions
------------

1.  Retrieve the JAR File and sample credentials CSV.
2.  Follow the steps outlined at the [SN Wiki][More information available at this SN Wiki Article] to install

  [More information available at this SN Wiki Article]: http://wiki.servicenow.com/index.php?title=External_Credential_Storage
