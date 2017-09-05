package org.jrpc.transport.consumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jrpc.transport.JChannelGroup;

public class RoundRobinLoadBalance implements LoadBalance<JChannelGroup>{

	private static final AtomicInteger index = new AtomicInteger(0);

	public JChannelGroup select(List<JChannelGroup> channelGroups) {
		if(index.get() >= channelGroups.size()) {
			index.set(0);
		}
		return channelGroups.get(index.getAndIncrement());
	}

}
