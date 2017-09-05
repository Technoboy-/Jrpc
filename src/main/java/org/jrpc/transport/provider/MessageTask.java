/**
 * 
 */
package org.jrpc.transport.provider;

import org.jrpc.consumer.MessageWrapper;
import org.jrpc.consumer.Status;
import org.jrpc.provider.Provider;
import org.jrpc.serializer.SerializerFactory;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JRequest;
import org.jrpc.transport.JResponse;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class MessageTask implements Runnable{
	
	private final JChannel channel; 
	
	private final JRequest request;

	public MessageTask(JChannel channel, JRequest request) {
		this.channel = channel;
		this.request = request;
	}

	public void run() {
		request.setMessage(SerializerFactory.FASTJSON.deserialize(request.getBody(), MessageWrapper.class));
		Object result = Provider.invoke(request.getMessage());
		JResponse response = new JResponse();
		ResultWrapper resultWrapper = new ResultWrapper(result);
		response.setId(request.getId());
		response.setStatus(Status.OK.value());
		response.setBody(SerializerFactory.FASTJSON.serialize(resultWrapper));
		channel.write(response);
	}
	
}
