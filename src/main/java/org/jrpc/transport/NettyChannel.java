/**
 * 
 */
package org.jrpc.transport;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class NettyChannel implements JChannel{
	
	private static final AttributeKey<NettyChannel> channelKey = AttributeKey.valueOf("channel.key");

	public static JChannel attachChannel(Channel channel) {
		Attribute<NettyChannel> attr = channel.attr(channelKey);
		NettyChannel oldChannel = attr.get();
		if(oldChannel == null){
			NettyChannel nettyChannel = new NettyChannel(channel);
			oldChannel = attr.setIfAbsent(nettyChannel);
			if(oldChannel == null){
				oldChannel = nettyChannel;
			}
		}
		return oldChannel;
	}

	protected Channel channel;
	
	protected NettyChannel(Channel channel){
		this.channel = channel;
	}

	public void write(Object obj) {
		this.channel.writeAndFlush(obj);
	}
}
