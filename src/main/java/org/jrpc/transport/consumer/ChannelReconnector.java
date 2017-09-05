package org.jrpc.transport.consumer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jrpc.transport.JAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ChannelReconnector {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelReconnector.class);

	protected final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private volatile Channel channel;
	
	private final Bootstrap bootstrap;
	
	private JAddress address;
	
	private final AtomicBoolean reconnect = new AtomicBoolean(true);
	
	public ChannelReconnector(Bootstrap bootstrap){
		this.bootstrap = bootstrap;
	}
	
	public void setAddress(JAddress address) {
		this.address = address;
	}

	public void setReconnect(boolean reconnect){
		this.reconnect.set(reconnect);
	}
	
	public void start(){
		startRechduler();
		connect();
	}
	
	public void close(){
		channel.close();
		scheduler.shutdown();
	}

	private void startRechduler(){
		scheduler.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				try {
					if (reconnect.get() && !isConnected()) {
						connect();
						LOGGER.warn("reconnected...");
					}
				} catch (Throwable e) {
					LOGGER.error("client socket reconnect Throwable.", e);
				}
			}
		}, 5 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}
	
	private void connect() {
		ChannelFuture future = null;
		try {
			future = bootstrap.connect(address.getHost(), address.getPort());
			boolean connected = future.awaitUninterruptibly(5000, TimeUnit.MILLISECONDS);
			if (connected && future.isSuccess()) {
				Channel newChannel = future.channel();
				try {
					Channel oldChannel = channel;
					if (oldChannel != null) {
						oldChannel.close();
					}
				} finally {
					channel = newChannel;
				}
			} else if (future.cause() != null) {
				LOGGER.error("connect " + "server(ip:" + address.getHost() + ", port:" + address.getPort() + ") error");
			} else {
				LOGGER.error("connect " + "server(ip:" + address.getHost() + ", port:" + address.getPort() + ") timeout");
			}
		} finally {
			if (!isConnected()) {
				future.cancel(true);
			}
		}
	}
	
	private boolean isConnected() {
		if (channel != null) {
			return channel.isActive();
		}
		return false;
	}
	
	public Channel channel(){
		return this.channel;
	}
}
