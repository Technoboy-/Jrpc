/**
 * 
 */
package org.jrpc.transport;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JAddress {

	private String host;
	
	private int port;
	
	public JAddress(){
		
	}
	
	public JAddress(String host, int port){
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "JAddress [host=" + host + ", port=" + port + "]";
	}
}
