/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.transport.JChannel;
import org.jrpc.transport.JChannelGroup;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class NettyChannelGroup implements JChannelGroup{

	private JChannel channel;
	
	public void bind(JChannel channel) {
		this.channel = channel;
	}

	public void close() {
		
	}

	public JChannel channel() {
		return this.channel;
	}

}
