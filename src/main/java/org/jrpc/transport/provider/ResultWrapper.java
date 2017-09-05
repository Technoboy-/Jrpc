/**
 * 
 */
package org.jrpc.transport.provider;

import java.io.Serializable;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ResultWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private Object result;
	private String error;
	
	public ResultWrapper(){
		//
	}
	
	public ResultWrapper(Object result){
		this.result = result;
	}
	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
