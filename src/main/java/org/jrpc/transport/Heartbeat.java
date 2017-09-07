package org.jrpc.transport;

import java.io.Serializable;

import org.jrpc.common.JConstants;

public class Heartbeat extends JPacket implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Heartbeat newInstance(){
		Heartbeat beat = new Heartbeat();
		beat.setId(0);
		beat.setSign(JConstants.HEARTBEAT);
		byte[] body = new byte[0];
		beat.setBody(body);
		return beat;
	}
	
}
