/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.transport.JChannel;
import org.jrpc.transport.JResponse;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface ConsumerProcessor {

	void handleResponse(JChannel channel, JResponse response);
}
