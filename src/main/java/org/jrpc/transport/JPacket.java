/**
 * 
 */
package org.jrpc.transport;

import java.io.Serializable;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final Short MAGIC = (short)0xbabe;
	
	public static final int LENGTH = 2 + 1 + 1 + 8 + 4;
	
	private byte sign;
	
	private byte status;
	
	private long id;
	
	private byte[] body;
	
	public JPacket(){
		//
	}
	
	public JPacket(byte sign, byte status, long id, byte[] body){
		this.id = id;
		this.status = status;
		this.body = body;
	}

	public byte getSign() {
		return sign;
	}

	public void setSign(byte sign) {
		this.sign = sign;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
}
