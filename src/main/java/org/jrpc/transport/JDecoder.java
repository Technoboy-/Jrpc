/**
 * 
 */
package org.jrpc.transport;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JDecoder extends ByteToMessageDecoder{
	
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < JPacket.LENGTH){
			return;
		}
		in.markReaderIndex();
		short magic = in.readShort();
		if(magic != JPacket.MAGIC){
			ctx.close();
		}
		byte sign = in.readByte();
		byte status = in.readByte();
		long id = in.readLong();
		int len = in.readInt();
		if(in.readableBytes() < len){
			in.resetReaderIndex();
			return;
		}
		byte[] body = new byte[len];
		in.readBytes(body);
		JPacket packet = new JPacket();
		packet.setSign(sign);
		packet.setStatus(status);
		packet.setId(id);
		packet.setBody(body);
		out.add(packet);
	}

}
 