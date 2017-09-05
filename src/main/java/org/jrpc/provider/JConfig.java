/**
 * 
 */
package org.jrpc.provider;

import org.jrpc.util.NetUtils;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JConfig {
	
	private String zkServerList;

	private int port = 20881;
	
	private String ip = NetUtils.getLocalIp();
	
	public String getZkServerList() {
		return zkServerList;
	}

	public void setZkServerList(String zkServerList) {
		this.zkServerList = zkServerList;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	@Override
	public String toString() {
		return "ProviderConfig [zkServerList=" + zkServerList + ", port=" + port + ", ip=" + ip + "]";
	}

}
