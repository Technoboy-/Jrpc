package org.jrpc.transport.consumer;

import java.util.List;

public interface LoadBalance<T> {
	
	public T select(List<T> clients);
}
