/**
 * 
 */
package org.jrpc.test;

import org.jrpc.common.ServiceProvider;

/**
 * @author caoguo(jiwei.guo)
 *
 */
@ServiceProvider(version="1.0.0", group="JRpc")
public interface IHello {

	public String sayHello();
	
	public String sayGoodbye();
	
	public String sayName(String name);
}
