package org.jrpc.transport;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface JChannelGroup {

	JChannel channel();
	
	void close();
}
