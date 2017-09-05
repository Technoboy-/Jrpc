/**
 * 
 */
package org.jrpc.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JEncoder extends MessageToByteEncoder<JPacket>{

	protected void encode(ChannelHandlerContext ctx, JPacket msg, ByteBuf out) throws Exception {
		out.writeShort(JPacket.MAGIC);
		out.writeByte(msg.getSign());
		out.writeByte(msg.getStatus());
		out.writeLong(msg.getId());
		out.writeInt(msg.getBody().length);
		out.writeBytes(msg.getBody());
	}

}
