/**
 * 
 */
package org.jrpc.transport.provider;

import org.jrpc.transport.JChannel;
import org.jrpc.transport.JRequest;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface ProviderProcessor {

	void handleRequest(JChannel channel, JRequest request);
}
