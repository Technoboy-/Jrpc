/**
 * 
 */
package org.jrpc.transport.consumer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jrpc.common.JConstants;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JResponse;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class DefaultConsumerProcessor implements ConsumerProcessor {
	
	private final Executor executor;
	
	public DefaultConsumerProcessor(){
		// TODO  SPI threadpool
		executor = Executors.newFixedThreadPool(JConstants.CPU_SIZE);
	}

	public void handleResponse(JChannel channel, JResponse response) {
		MessageTask task = new MessageTask(channel, response);
		if(executor == null){
			task.run();
		} else{
			executor.execute(task);
		}
	}

}
