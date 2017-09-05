/**
 * 
 */
package org.jrpc.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;



/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JdkProxy implements InvocationHandler{
	
	private Class clazz;
	
	public JdkProxy(Class clazz){
		this.clazz = clazz;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
//		
//		
//		SyncResponse response = new SyncResponse(packet.getId());
//		RequestMap.put(packet.getId(), response);
//		
//		return response.get();
		return null;
	}

}
