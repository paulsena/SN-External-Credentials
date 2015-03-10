package com.snc.discovery;

class Constants {
	
	// These are the permissible names of arguments passed INTO the resolve() method.
	 
	// the string identifier as configured on the ServiceNow instance...
	public static final String ARG_ID   = "id";    
 
	// a dotted-form string IPv4 address (like "10.22.231.12") of the target system...
	public static final String ARG_IP   = "ip"; 
 
	// the string type (ssh, snmp, etc.) of credential as configured on the instance...
	// Argument will be one of: 
	// "ssh_password","ssh_private_key","snmp","vmware","windows","mssql","cim"
	public static final String ARG_TYPE = "type"; 
 
	// the string MID server making the request, as configured on the instance...
	public static final String ARG_MID  = "mid";  
 

	// These are the permissible names of values returned FROM the resolve() method.
 
	// the string user name for the credential, if needed...
	public static final String VAL_USER = "user"; 
 
	// the string password for the credential, if needed...
	public static final String VAL_PSWD = "pswd"; 
 
	// the string private key for the credential, if needed...
	public static final String VAL_PKEY = "pkey"; 
 
	// the string pass phrase for the credential if needed:
	public static final String VAL_PASSPHRASE = "passphrase";

}
