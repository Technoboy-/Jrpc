/**
 * 
 */
package org.jrpc.transport.provider;

import org.jrpc.transport.JPacket;
import org.jrpc.transport.JRequest;
import org.jrpc.transport.NettyChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ProviderHandler extends ChannelInboundHandlerAdapter{

	
	private final ProviderProcessor processor;
	
	public ProviderHandler(ProviderProcessor processor){
		this.processor = processor;
	}
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof JPacket){
			JPacket packet = (JPacket)msg;
			processor.handleRequest(NettyChannel.attachChannel(ctx.channel()), new JRequest(packet.getSign(), packet.getStatus(), packet.getId(), packet.getBody()));
		} else{
			//
		}
	}
}
