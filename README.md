SN-External-Credentials
=======================

A java class for Service Now's External Credentials Plugin.  Allows for machine's UN/PWs for discovery to be stored locally at a client's Mid Server rather then in the cloud in Service Now. 



== Overview ==
Sometimes clients would prefer not to store credentials for local networked machines in Service Now's instance for security reasons.  These credentials are used by Discovery and Orchestration when the MID server launches certain probes that need access to a client's machines and have to log on through SSH, SNMP or Windows.  

A ServiceNow instance can store credentials used by Discovery and Orchestration on an external credential repository rather than directly in a ServiceNow credentials record. The instance maintains a unique identifier for each credential, the credential type (such as SSH, SNMP, or Windows), and any credential affinities. The MID Server obtains the credential identifier from the instance, and then uses a customer-provided JAR file to resolve the identifier from the repository into a usable credential
[http://wiki.servicenow.com/index.php?title=External_Credential_Storage More information available at this SN Wiki Article]

Fruition Partners has developed a plugin (JAR file) which allows credential retrieval from a CSV file.  The CSV file is encrypted and un-encrypted with Windows Data Protection ([(http://msdn.microsoft.com/en-us/library/ms995355.aspx MS Documentation]).  This DPAPI (Data Protection API) uses a Triple-DES algorithm with a strong key to encrypt the data.  It uses API calls available in Windows 2000 and later to accomplish this.  The strong key used is the current user's password hash stored in windows.  So, no password management or entry is needed.  Since the JAR file is run by the MID server, the CSV file will be un-encrypted by the windows password hash of the service account the MID server is running as.


'''Note'''

*This Java code can be modified to not use encryption if not needed by a client.  The file can be locked down in Windows through ACL rules and by the assumption that the files on the internal server would not be compromised.
*This integration needs a few more hours for testing and polishing out, as it was developed for a client but never implemented.


== Benefits (For Marketing) ==
* Provides high security clients ability store sensitive account credentials (used for Discovery and Orchestration) locally within their network instead of in SN's databases which are a hosted, offsite solution.
* Sensitive account credentials for an organizations machines are stored securely in a Triple-DES encrypted CSV file, locally on your MID Server's file system.
* Account credentials are loaded into memory and de-crypted only at run time when Discovery and Orchestration probes are sent to the MID server when work is needed.
* Store SSH, SNMP, and Windows credentials.


== Technical Diagrams ==
DPAPI Encryption.  The JAR file would be the "Application" here:

[[File:DPAPI.jpg]]


== Instructions ==
# Retrieve the Fruition Partner's JAR File and sample credentials CSV.
# Follow the steps outlined at the [http://wiki.servicenow.com/index.php?title=External_Credential_Storage SN Wiki] to install this JAR and setup SN to use External Credentials.
# Copy your credentials CSV to the root directory of the MID server installation.
# Edit your credentials CSV with proper ID, Username, and Password values for each entry.
# We need to encrypt the CSV file now that we've made our updates. <br/>From a command prompt in the root Midserver agent folder, run <Code>java lib/ExternCreds.jar encrypt</Code>
# For future updates to the CSV, un-encrypt first by running <Code>java lib/ExternCreds.jar decrypt</Code>


== Source Code ==
# [https://drive.google.com/a/fruitionpartners.com/file/d/0B92Tet3Z8b5FcjkwMWt5WEZSVFU/edit?usp=sharing Source Code]


== Files ==
# [https://drive.google.com/a/fruitionpartners.com/file/d/0B92Tet3Z8b5FNWp2TnBEM3Zlb0U/edit?usp=sharing JAR File]
# [https://drive.google.com/a/fruitionpartners.com/file/d/0B92Tet3Z8b5FTFltRXBralV4ZkU/edit?usp=sharing Sample credentials.csv]


== Contact ==
*'''Developer: ''' Paul Senatillaka <paul.senatillaka@fruitionpartners.com>
*'''Code Maintainer/Owner: ''' Integrations Team. Paul Senatillaka <paul.senatillaka@fruitionpartners.com>
*'''Product Owner: ''' TBD When Productized


[[Category:Integrations]]
[[Category:Discovery]]
