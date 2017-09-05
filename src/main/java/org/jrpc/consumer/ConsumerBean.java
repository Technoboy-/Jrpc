/**
 * 
 */
package org.jrpc.consumer;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ConsumerBean {

	private String methodName;

	private Class target;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class getTarget() {
		return target;
	}

	public void setTarget(Class target) {
		this.target = target;
	}

}
