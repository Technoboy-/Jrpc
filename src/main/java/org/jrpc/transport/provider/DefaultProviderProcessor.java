/**
 * 
 */
package org.jrpc.transport.provider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jrpc.common.JConstants;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JRequest;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class DefaultProviderProcessor implements ProviderProcessor {
	
	private final Executor executor;
	
	public DefaultProviderProcessor(){
		//TODO SPI threadpool
		executor = Executors.newFixedThreadPool(JConstants.CPU_SIZE);
	}

	public void handleRequest(JChannel channel, JRequest request) {
		MessageTask task = new MessageTask(channel, request);
		if(executor == null){
			task.run();
		} else{
			executor.execute(task);
		}
	}

}
