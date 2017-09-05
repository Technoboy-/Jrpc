/**
 * 
 */
package org.jrpc.transport;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import org.jrpc.common.JConstants;
import org.jrpc.consumer.MessageWrapper;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JRequest extends JPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static AtomicLong requestId = new AtomicLong(0);
	
	private MessageWrapper message;
	
	public JRequest(){
		this(requestId.getAndIncrement());
		setSign(JConstants.REQUEST);
	}
	
	public JRequest(byte sign, byte status, long id, byte[] body){
		super(sign, status, id, body);
	}
	
	public JRequest(long invokeId){
		setId(invokeId);
	}

	public MessageWrapper getMessage() {
		return message;
	}

	public void setMessage(MessageWrapper message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "JRequest [message=" + message + "]";
	}
	
}
