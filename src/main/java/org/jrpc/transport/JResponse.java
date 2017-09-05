/**
 * 
 */
package org.jrpc.transport;

import java.io.Serializable;

import org.jrpc.common.JConstants;
import org.jrpc.transport.provider.ResultWrapper;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JResponse extends JPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	private ResultWrapper result;
	
	public JResponse(){
		setSign(JConstants.RESPONSE);
	}
	
	public JResponse(byte status, long id, ResultWrapper resultWrapper){
		setSign(JConstants.RESPONSE);
		setStatus(status);
		setId(id);
		setResult(resultWrapper);
	}

	public JResponse(byte sign, byte status, long id, byte[] body){
		super(sign, status, id, body);
	}
	
	public ResultWrapper getResult() {
		return result;
	}

	public void setResult(ResultWrapper result) {
		this.result = result;
	}

}
