/**
 * 
 */
package org.jrpc.consumer;

import java.lang.reflect.Method;

import org.jrpc.consumer.dispatch.Dispatcher;
import org.jrpc.transport.consumer.JClient;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class SyncInvoker implements MethodInterceptor{
	
	private final Dispatcher dispatcher;
	
	private final JClient client;
	
	private final Method[] methods = Object.class.getDeclaredMethods();
	
	private final Object object = new Object();
	
	public SyncInvoker(Dispatcher dispatcher, JClient client){
		this.dispatcher = dispatcher;
		this.client = client;
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if(isNative(method)){
			return method.invoke(object, args);
		}
		InvokePromise<?> promise = dispatcher.dispatch(client, method.getName(), args);
		return promise.getResult();
	}
	
	private boolean isNative(Method method) throws Throwable{
		for(Method m: methods){
			if(method.getName().equals(m.getName())){
				return true;
			}
		}
		return false;
	}
}
