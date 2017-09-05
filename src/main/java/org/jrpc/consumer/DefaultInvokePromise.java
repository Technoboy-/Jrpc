/**
 * 
 */
package org.jrpc.consumer;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jrpc.transport.JChannel;
import org.jrpc.transport.JRequest;
import org.jrpc.transport.JResponse;
import org.jrpc.transport.provider.ResultWrapper;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class DefaultInvokePromise extends InvokePromise<Object> {
	
	private static final ConcurrentHashMap<Long, DefaultInvokePromise> promises = new ConcurrentHashMap<>();
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	private final JRequest request;
	private final Long id;
	private final long timeoutMillis;
	private final long startMillis;
	
	private JResponse response;
	private JChannel channel;
	
	public DefaultInvokePromise(JChannel channel, JRequest request, long timeoutMillis){
		this.channel = channel;
		this.request = request;
		this.id = request.getId();
		this.timeoutMillis = timeoutMillis;
		startMillis = System.currentTimeMillis();
		promises.put(this.request.getId(), this);
	}
	
	public static void receive(JChannel channel, JResponse response){
		long id = response.getId();
		DefaultInvokePromise promise = promises.remove(id);
		if(promise != null){
			promise.doReceive(response);
		}
	}
	
	private void doReceive(JResponse response) {
		this.response = response;
		latch.countDown();
	}

	public Object getResult() throws Throwable {
		try {
			latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resultFromResponse();
	}

	private Object resultFromResponse() {
		final JResponse _response = this.response;
		byte status = _response.getStatus();
		if(status == Status.OK.value()){
			ResultWrapper result = _response.getResult();
			return result.getResult();
		} else{
			throw new RemoteException();
		}
	}
	
	public long getTimeoutMillis() {
		return timeoutMillis;
	}
	
	public long getStartMillis() {
		return startMillis;
	}
	
	static class TimeoutScanner implements Runnable{

		public void run() {
			for(;;){
				Collection<DefaultInvokePromise> values = promises.values();
				for(DefaultInvokePromise promise : values){
					if(System.currentTimeMillis() - promise.getStartMillis() > promise.getTimeoutMillis()){
						processTimeout(promise);
					}
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
				}
			}
		}
		private void processTimeout(DefaultInvokePromise promise){
			ResultWrapper resultWrapper = new ResultWrapper();
			resultWrapper.setError("timeout request");
			JResponse response = new JResponse(Status.CLIENT_TIMEOUT.value(), promise.id, resultWrapper);
			DefaultInvokePromise.receive(promise.channel, response);
		}
	}
	
	static{
		Thread thread = new Thread(new TimeoutScanner(), "TimeoutScanner");
		thread.setDaemon(true);
		thread.start();
	}
}
