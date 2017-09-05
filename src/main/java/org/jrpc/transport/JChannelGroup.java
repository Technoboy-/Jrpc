/**
 * 
 */
package org.jrpc.transport;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface JChannelGroup {

	void bind(JChannel channel);
	
	JChannel channel();
	
	void close();
}
