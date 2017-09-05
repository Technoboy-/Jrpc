package org.jrpc.transport.consumer;

import java.util.List;
import java.util.Random;

import org.jrpc.transport.JChannelGroup;

public class RandomLoadBalance implements LoadBalance<JChannelGroup>{

	private final Random random = new Random();
	
	public JChannelGroup select(List<JChannelGroup> clients) {
		int nextInt = random.nextInt(clients.size());
		return clients.get(nextInt);
	}
}
