package org.jrpc.transport.consumer;

import org.jrpc.transport.JAddress;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JChannelGroup;
import org.jrpc.transport.NettyChannel;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class NettyChannelGroup implements JChannelGroup{

	private final String host;
	private final int port;
	
	private ChannelReconnector channelReconnector;
	
	public NettyChannelGroup(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public void start(){
		channelReconnector.setAddress(new JAddress(getHost(), getPort()));
		channelReconnector.start();
	}

	public void close() {
		channelReconnector.close();
	}

	public ChannelReconnector getChannelReconnector() {
		return channelReconnector;
	}

	public void setChannelReconnector(ChannelReconnector channelReconnector) {
		this.channelReconnector = channelReconnector;
	}

	public JChannel channel() {
		return NettyChannel.attachChannel(channelReconnector.channel());
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NettyChannelGroup other = (NettyChannelGroup) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
