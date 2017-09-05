/**
 * 
 */
package org.jrpc.transport.provider;

import org.jrpc.transport.JDecoder;
import org.jrpc.transport.JEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class JServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JServer.class);
	
	private ServerBootstrap bootstrap;
	
	private EventLoopGroup bossGroup;
	
	private EventLoopGroup workGroup;
	
	private final ProviderProcessor processor = new DefaultProviderProcessor();
	
	private int port;
	
	public JServer(int port){
		this.port = port;
		init();
	}
	
	private void init(){
		bootstrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(1);
		workGroup = new NioEventLoopGroup(1);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true).
				  option(ChannelOption.TCP_NODELAY, true);
		bootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {

					protected void initChannel(NioSocketChannel ch) throws Exception {
						initNettyChannel(ch);
					}
		});
	}
	
	public void start(){
		ChannelFuture cf = null;
		try {
			cf = bootstrap.bind(this.port).sync();
		} catch (InterruptedException e) {
			LOGGER.error("JServer bind fail {}", e);
		}
		if (cf.isSuccess()) {
			LOGGER.info("JServer bind success at port : {}", 20881);
		} else if (cf.cause() != null) {
			LOGGER.error("JServer bind fail {}", cf.cause());
		} else {
			LOGGER.error("JServer bind fail, exit");
			System.exit(1);
		}
	}
	
	protected void initNettyChannel(NioSocketChannel ch) throws Exception{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("encoder", new JEncoder());
		pipeline.addLast("decoder", new JDecoder());
		pipeline.addLast("handler", new ProviderHandler(processor));
	}
}
