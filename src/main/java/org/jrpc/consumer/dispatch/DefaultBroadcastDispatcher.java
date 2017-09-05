/**
 * 
 */
package org.jrpc.consumer.dispatch;

import org.jrpc.consumer.InvokePromise;
import org.jrpc.consumer.ServiceMetadata;
import org.jrpc.transport.consumer.JClient;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class DefaultBroadcastDispatcher implements Dispatcher {

	/**
	 * @param metadata
	 */
	public DefaultBroadcastDispatcher(ServiceMetadata metadata) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.jrpc.consumer.Dispatcher#dispatch(org.jrpc.transport.consumer.JClient, java.lang.String, java.lang.Object[])
	 */
	@Override
	public InvokePromise<?> dispatch(JClient client, String methodName, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
