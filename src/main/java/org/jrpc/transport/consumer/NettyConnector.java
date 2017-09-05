/**
 * 
 */
package org.jrpc.transport.consumer;

import org.jrpc.provider.JConfig;
import org.jrpc.transport.JAddress;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JDecoder;
import org.jrpc.transport.JEncoder;
import org.jrpc.transport.NettyChannel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class NettyConnector extends AbstractClient{
	
	private Bootstrap bootstrap;

	private NioEventLoopGroup workGroup;
	
	private final ConsumerProcessor processor = new DefaultConsumerProcessor();
	
	private final JConfig config;
	
	public NettyConnector(JConfig config){
		super(config.getZkServerList());
		this.config = config;
		bootstrap = new Bootstrap();
		workGroup = new NioEventLoopGroup(1);
		bootstrap
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.group(workGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					
					protected void initChannel(NioSocketChannel ch) throws Exception {
						
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("encoder", new JEncoder());
						
						//in
						pipeline.addLast("decoder", new JDecoder());
						pipeline.addLast("handler", new ConsumerHandler(processor));
					}
				});
	}
	
	public JChannel connect(String ip, int port) {
		ChannelReconnector channelReconnector = new ChannelReconnector(bootstrap, new JAddress(ip, port));
		channelReconnector.start();
		return NettyChannel.attachChannel(channelReconnector.channel());
	}
	
	public void close() {
		workGroup.shutdownGracefully();
	}
}
