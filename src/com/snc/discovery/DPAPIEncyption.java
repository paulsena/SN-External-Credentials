package com.snc.discovery;

import net.sourceforge.jdpapi.DataProtector;

final class DPAPIEncryption {

	private final DataProtector protector;
	
	public DPAPIEncryption() {
		protector = new DataProtector();
		
	}
	
    static {
        System.loadLibrary("jdpapi-native-1.0.1");
    }
	
}
