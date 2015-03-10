package com.snc.discovery;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.*;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.sourceforge.jdpapi.DataProtector;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import static com.snc.discovery.Constants.*;
 
/**
 * Implements a credential resolver.  Given a credential identifier and other metadata, 
 * this resolver will return an actual credential.  This credential may be obtained by 
 * any means whatsoever.
 * 
 * Note that the fully qualified name of this class must be exactly:
 *    com.snc.discovery.CredentialResolver
 *    
 * This class must have a no-arguments constructor, and this must be the only 
 * constructor.  The MID server will instantiate this class when needed, and call its 
 * single public method to resolve a credential.  The MID server will NOT call a single
 * instance of this class from multiple threads.  The MID server MAY call a single instance 
 * of this class multiple times from the same thread.
 * 
 * @author Paul Senatillaka paul.senatillaka@fruitionpartners.com
 * 
 * Original empty stub file take from: http://wiki.servicenow.com/index.php?title=External_Credential_Storage 
 * @author Tom Dilatush  tom.dilatush@service-now.com
 * @author Eugene Lockett eugene.lockett@service-now.com
 * 
 */
public class CredentialResolver {
 
	
	public Map<String, String[]> storedCredentials = new HashMap<String, String[]>();;
	
	private final static Logger LOGGER = Logger.getLogger(CredentialResolver.class.getName()); 
	
	private final DataProtector dpapiEncryption = new DataProtector();
	
 
	//Constructor
	public CredentialResolver() {
			
		CSVReader reader;
		FileHandler handler;
		
		//Init Logger
		try {
			handler = new FileHandler("logs/ExternalCredential_log.txt", 50000, 1, true);
			handler.setFormatter(new SimpleFormatter());
			handler.setLevel(Level.ALL);
			LOGGER.addHandler(handler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Load Credentials CSV.  Format: ID, Username, Password
		try {
			reader = new CSVReader(new FileReader("credentials.csv"));
			String[] nextLine;
			
			while ((nextLine = reader.readNext()) != null) {
				String[] credentials = new String[2];
				credentials[0] = nextLine[1];
				credentials[1] = nextLine[2];
				storedCredentials.put(nextLine[0], credentials);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.severe("credentials.csv not found");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Error parsing CSV", e);
		}
	    
	}
 
	/**
	 * Resolve a credential.
	 */
	public Map<String, String> resolve(Map<String, String> args) {
		
	    // First grab the arguments from the map
	    String credentialId = (String) args.get(ARG_ID);	    
	    String credentialType = (String) args.get(ARG_TYPE);
	    //String thisMidServerName = (String) args.get(ARG_MID);
	    //String targetIp = (String) args.get(ARG_IP);
 
	    // Variables for returning to the MID server
	    String user = new String(), password = new String(), passphrase = new String(), privateKey = new String();
 
	    // Check the requested type and make the appropriate calls to the credential repository:
            // Possible type values are "ssh_password, ssh_private_key, snmp, windows, vmware, mssql, cim and etc"
	    if(credentialType == "ssh_private_key") {
    	    // TODO: make call to to credential repository to grab user, passphrase and privateKey
	    	LOGGER.severe("Attempted to lookup SSH Private Key. Not Supported. Only SSH and Windows UN/PWDs are in the CSV file.");
	    }
	    else {
    	    // TODO: make call to credential repository to grab user and password
	    	String[] retrievedCredential = storedCredentials.get(credentialId);
	    	if (retrievedCredential != null) {
	    		user = retrievedCredential[0];
	    		password = retrievedCredential[1];
	    		LOGGER.info("Credential found for ID: " + credentialId);
	    	}
	    	else {
	    		LOGGER.warning("Credential not found for ID: " + credentialId);
	    	}
	    }
	    
	    //Unencrypt data
    	String unencryptedPwd = null;
    	String unencryptedUser = null;
		try {
			unencryptedPwd = dpapiEncryption.unprotect(Base64.decode(password));
			unencryptedUser = dpapiEncryption.unprotect(Base64.decode(user));
		} catch (Base64DecodingException e) {
			// TODO Auto-generated catch block
			LOGGER.severe("Error Base64 decoding enrypted string. Message: " + e.getMessage());
		}
    	
LOGGER.info("Decrypted: " + unencryptedUser + " " + unencryptedPwd);
 
	    // Finally, the resolved credential is returned in a HashMap...
		Map<String, String> result = new HashMap<String, String>();
	    result.put(VAL_USER, unencryptedUser);
	    result.put(VAL_PSWD, unencryptedPwd);
	    result.put(VAL_PASSPHRASE, passphrase);
	    result.put(VAL_PKEY, privateKey);
	    return result;
	}
 
        /**
         * Return the API version supported by this class.
         */
        public String getVersion() {
            return "1.0";
        }
        
        public static void main(String[] args) {
        
        	if (args[0].equals("decrypt")) {
        		decrypt(args);
        	} else if (args[0].equals("encrypt")){
    			encrypt(args);
        	} else if (args[0].equals("test")) {
        		CredentialResolver test = new CredentialResolver();
    			Map<String, String> testArgs = new HashMap<String, String>();
    			testArgs.put(ARG_ID, args[1]);
    			System.out.println(test.resolve(testArgs));
        	}
        	
        }
        
        private static void decrypt (String[] args) {
        	System.out.println("Decrypting credentials.csv");
        	CredentialResolver credResolver = new CredentialResolver();
        	
        	try {
				CSVWriter writer = new CSVWriter(new FileWriter("credentials.csv"));
				List<String> outputLine = null;
				Iterator<Entry<String, String[]>> it = credResolver.storedCredentials.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String,String[]> pairs = (Map.Entry<String,String[]>)it.next();
					outputLine.add(pairs.getKey());
					outputLine.add(pairs.getValue()[0]);
					outputLine.add(pairs.getValue()[1]);
					String[] a = null;
System.out.println("Decrypted Line: " + outputLine.toArray(a));
					writer.writeNext(outputLine.toArray(a));
					
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        
        private static void encrypt (String[] args) {
        	System.out.println("Encrypting credentials.csv");
        	CredentialResolver credResolver = new CredentialResolver();

        	
        	try {
				CSVWriter writer = new CSVWriter(new FileWriter("credentials.csv"));
				List<String> outputLine = null;
				Iterator<Entry<String, String[]>> it = credResolver.storedCredentials.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String,String[]> pairs = (Map.Entry<String,String[]>)it.next();
					
					outputLine.add(pairs.getKey());
					outputLine.add(pairs.getValue()[0]);
					outputLine.add(pairs.getValue()[1]);
					String[] a = null;
System.out.println("Decrypted Line: " + outputLine.toArray(a));
					writer.writeNext(outputLine.toArray(a));
					
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        
        static {
            System.loadLibrary("jdpapi-native-1.0.1");
        }


        
}