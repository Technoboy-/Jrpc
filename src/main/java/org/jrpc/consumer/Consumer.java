/**
 * 
 */
package org.jrpc.consumer;

import java.util.HashMap;
import java.util.Map;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public class Consumer {
	
	private static final Map<String, Object> beans = new HashMap<>();

	private final CglibProxy proxy = new CglibProxy();
	
	public void register(Class target){
	}
	
	public Object getBean(String className){
		return beans.get(className);
	}
}
