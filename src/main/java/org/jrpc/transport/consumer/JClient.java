/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.consumer.ServiceMetadata;
import org.jrpc.registry.RegisterMeta.Address;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JChannelGroup;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface JClient {
	
	public void subcribe(ServiceMetadata metadata);

	JChannel select(ServiceMetadata metadata);
	
	JChannelGroup group(Address address);
	
}
