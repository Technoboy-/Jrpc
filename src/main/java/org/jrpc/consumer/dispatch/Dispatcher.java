/**
 * 
 */
package org.jrpc.consumer.dispatch;

import org.jrpc.consumer.InvokePromise;
import org.jrpc.transport.consumer.JClient;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface Dispatcher {

	InvokePromise<?> dispatch(JClient client, String methodName, Object[] args);
	
}
