/**
 * 
 */
package org.jrpc.test;

import org.jrpc.consumer.ProxyFactory;
import org.jrpc.provider.JConfig;
import org.jrpc.provider.Provider;
import org.jrpc.transport.consumer.JClient;
import org.jrpc.transport.consumer.NettyConnector;
import org.jrpc.transport.provider.JServer;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class Test {

	
	public static void main(String[] args) {
		test();
		
	}
	
	private static void test(){
		JConfig config = new JConfig();
		config.setPort(20881);
		config.setZkServerList("10.1.21.95:2181");
		JServer server = new JServer(config.getPort());
		server.start();
		Provider provider = new Provider(config);
		HelloImpl helloImpl = new HelloImpl();
		provider.register(IHello.class, helloImpl);
		
		
		//
		JClient client = new NettyConnector(config);
		IHello hello = ProxyFactory.factory(IHello.class).setClient(client).newProxyInstance();
		hello.sayHello();
		hello.sayGoodbye();
		String sayName = hello.sayName("tboy");
		System.out.println(sayName);
		
	} 
}
