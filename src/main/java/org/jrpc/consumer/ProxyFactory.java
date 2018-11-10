/**
 * 
 */
package org.jrpc.consumer;

import org.jrpc.common.ServiceProvider;
import org.jrpc.consumer.dispatch.DefaultBroadcastDispatcher;
import org.jrpc.consumer.dispatch.DefaultRoundDispatcher;
import org.jrpc.consumer.dispatch.DispatchType;
import org.jrpc.consumer.dispatch.Dispatcher;
import org.jrpc.transport.consumer.JClient;
import org.jrpc.util.StringUtils;

/**
 * @author tboy(jiwei.guo)
 *
 */
public class ProxyFactory<I> {

	private final Class<I> interfaceClass;
	
	private DispatchType dispatchType = DispatchType.ROUND;
	
	private InvokeType invokeType = InvokeType.SYNC;
	
	private JClient client;
	
	public static <I> ProxyFactory<I> factory(Class<I> interfaceClass) {
        ProxyFactory<I> factory = new ProxyFactory<>(interfaceClass);
        return factory;
    }

	private ProxyFactory(Class<I> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
	
	public ProxyFactory<I> setClient(JClient client) {
		this.client = client;
		return this;
	}

	public I newProxyInstance(){
		ServiceProvider annotation = this.interfaceClass.getAnnotation(ServiceProvider.class);
		String providerName = annotation.value();
        providerName = StringUtils.isEmpty(providerName) ? interfaceClass.getName() : providerName;
		ServiceMetadata metadata = new ServiceMetadata(annotation.version(), annotation.group(), providerName);
		client.subcribe(metadata);
		Dispatcher dispatcher = asDispatcher(metadata);
		
		Object handler = null;
		switch (invokeType) {
			case SYNC:
				handler = new SyncInvoker(dispatcher, client);
				break;
			case ASYNC:
				
				break;
			case CALLBACK:
				
				break;
			default:
				throw new IllegalStateException("InvokeType : " + invokeType);
		}
		return Proxies.getDefault().newProxy(interfaceClass, handler);
	}

	protected Dispatcher asDispatcher(ServiceMetadata metadata) {
		switch (dispatchType) {
			case ROUND:
				return new DefaultRoundDispatcher(metadata);
			case BROADCAST:
				return new DefaultBroadcastDispatcher(metadata);
			default:
				throw new IllegalStateException("DispatchType : " + dispatchType);
		}
	}
}
