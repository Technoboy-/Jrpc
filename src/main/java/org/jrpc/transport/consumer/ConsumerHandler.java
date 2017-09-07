/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.transport.Heartbeat;
import org.jrpc.transport.JPacket;
import org.jrpc.transport.JResponse;
import org.jrpc.transport.NettyChannel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

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
	
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
            	System.out.println("heartbeat");
                ctx.writeAndFlush(Heartbeat.newInstance());
            }
        }
    }

}
