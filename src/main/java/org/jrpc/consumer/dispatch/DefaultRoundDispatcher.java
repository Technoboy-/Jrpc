/**
 * 
 */
package org.jrpc.consumer.dispatch;

import org.jrpc.consumer.DefaultInvokePromise;
import org.jrpc.consumer.InvokePromise;
import org.jrpc.consumer.MessageWrapper;
import org.jrpc.consumer.ServiceMetadata;
import org.jrpc.serializer.SerializerFactory;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JRequest;
import org.jrpc.transport.consumer.JClient;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class DefaultRoundDispatcher implements Dispatcher {
	
	private final ServiceMetadata metadata;

	public DefaultRoundDispatcher(ServiceMetadata metadata) {
		this.metadata = metadata;
	}

	public InvokePromise<?> dispatch(JClient client, String methodName, Object[] args) {
		
		MessageWrapper message = new MessageWrapper(metadata);
		message.setMethodName(methodName);
		message.setArgs(args);
		
		JRequest request = new JRequest();
		request.setMessage(message);
		request.setBody(SerializerFactory.FASTJSON.serialize(message));
		JChannel channel = client.select(metadata);
		channel.write(request);
		long timeoutMillis = 5 * 1000;
		InvokePromise<?> promise = asPromise(channel, request, timeoutMillis);
		return promise;
	}

	protected InvokePromise<?> asPromise(JChannel channel, JRequest request, long timeoutMillis) {
		return new DefaultInvokePromise(channel, request, timeoutMillis);
	}
}
