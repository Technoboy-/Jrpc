/**
 * 
 */
package org.jrpc.test;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class HelloImpl implements IHello{

	public String sayHello() {
		System.out.println("HelloImpl sayHello");
		return "sayHello";
	}

	public String sayGoodbye() {
		System.out.println("HelloImpl sayGoodbye");
		return "sayGoodbye";
	}

	public String sayName(String name) {
		System.out.println("sayName name" + name);
		if(name.equals("tboy")){
			return "tboy wish";
		} else if(name.equals("tgirl")){
			return "tgirl wish";
		}
		return "no name";
	}

}
