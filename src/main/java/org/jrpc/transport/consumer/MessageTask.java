/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.consumer.DefaultInvokePromise;
import org.jrpc.serializer.SerializerFactory;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JResponse;
import org.jrpc.transport.provider.ResultWrapper;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class MessageTask implements Runnable{
	
	private final JChannel channel; 
	
	private final JResponse response;

	public MessageTask(JChannel channel, JResponse response) {
		this.channel = channel;
		this.response = response;
	}

	public void run() {
		response.setResult(SerializerFactory.FASTJSON.deserialize(response.getBody(), ResultWrapper.class));
		DefaultInvokePromise.receive(channel, response);
	}
	
}
