package org.jrpc.transport.consumer;

import org.jrpc.provider.JConfig;
import org.jrpc.transport.JDecoder;
import org.jrpc.transport.JEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class NettyConnector extends AbstractClient{
	
	private Bootstrap bootstrap;

	private NioEventLoopGroup workGroup;
	
	private final ConsumerProcessor processor = new DefaultConsumerProcessor();
	
	private JConfig config;
	
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
						pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 60, 0));
						pipeline.addLast("handler", new ConsumerHandler(processor));
					}
				});
	}
	
	public ChannelReconnector newChannelReconnector(){
		return new ChannelReconnector(bootstrap);
	}
	
	public void close() {
		workGroup.shutdownGracefully();
	}
}
