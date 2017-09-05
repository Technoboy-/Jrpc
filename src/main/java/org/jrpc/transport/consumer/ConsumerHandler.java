/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.transport.JPacket;
import org.jrpc.transport.JResponse;
import org.jrpc.transport.NettyChannel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author caoguo(jiwei.guo)
 *
 */
@Sharable
public class ConsumerHandler extends ChannelInboundHandlerAdapter{

	
	private final ConsumerProcessor processor;
	
	public ConsumerHandler(ConsumerProcessor processor){
		this.processor = processor;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof JPacket){
			JPacket packet = (JPacket)msg;
			processor.handleResponse(NettyChannel.attachChannel(ctx.channel()), new JResponse(packet.getSign(), packet.getStatus(), packet.getId(), packet.getBody()));
		} else{
			//TODO
		}
	}
}
