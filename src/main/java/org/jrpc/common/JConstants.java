/**
 * 
 */
package org.jrpc.common;

import java.nio.charset.Charset;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JConstants {
	
	public static final Charset UTF8 = Charset.forName("UTF-8");
	
	public static final int CPU_SIZE = Runtime.getRuntime().availableProcessors();
	
	public static final byte REQUEST = 1;
	
	public static final byte RESPONSE = 2;
	
	public static final byte HEARTBEAT = 3;
	
	public static final String DEFAULT_GROUP = "JRpc";
	
	public static final String DEFAULT_VERSION = "1.0.0";
	
}
