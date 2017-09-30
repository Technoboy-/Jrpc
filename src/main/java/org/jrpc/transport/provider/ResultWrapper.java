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
	private Throwable exception;
	
	public ResultWrapper(){
		//
	}
	
	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public ResultWrapper(Object result){
		this.result = result;
	}
	
	public Object getResult() {
		if(exception != null) {
			return exception;
		}
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
