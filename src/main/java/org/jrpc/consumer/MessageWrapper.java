/**
 * 
 */
package org.jrpc.consumer;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class MessageWrapper implements Serializable{

	private static final long serialVersionUID = 1L;

	private String appName;
	
	private ServiceMetadata metadata;
	
	private String methodName;
	
	private Object[] args;
	
	public MessageWrapper(){
		//
	}
	
	public MessageWrapper(ServiceMetadata metadata){
		this.metadata = metadata;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public ServiceMetadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(ServiceMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "MessageWrapper [appName=" + appName + ", metadata=" + metadata + ", methodName=" + methodName
				+ ", args=" + Arrays.toString(args) + "]";
	}
}
