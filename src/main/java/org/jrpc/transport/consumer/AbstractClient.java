/**
 * 
 */
package org.jrpc.transport.consumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jrpc.consumer.ServiceMetadata;
import org.jrpc.registry.NotifyListener;
import org.jrpc.registry.RegisterMeta;
import org.jrpc.registry.RegisterMeta.Address;
import org.jrpc.registry.RegisterMeta.ServiceMeta;
import org.jrpc.registry.Registry;
import org.jrpc.registry.ZookeeperRegistry;
import org.jrpc.transport.JChannel;
import org.jrpc.transport.JChannelGroup;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public abstract class AbstractClient implements JClient{
	
	protected final Registry registry = new ZookeeperRegistry();
	
	private static final Map<ServiceMetadata, Set<JChannelGroup>> clientMap = new HashMap<>();
	
	private LoadBalance<JChannelGroup> loadBalance = new RoundRobinLoadBalance();
	
	private ConcurrentHashMap<Address, JChannelGroup> groups = new ConcurrentHashMap<>();
	
	public AbstractClient(String connectString){
		registry.conncectServer(connectString);
	}
	
	public void subcribe(ServiceMetadata metadata){
		registry.subscribe(asServiceMeta(metadata), new NotifyListener(){

			public void notify(RegisterMeta meta, NotifyEvent event) {
				JChannelGroup channelGroup = group(meta.getAddress());
				if(event == NotifyEvent.CHILD_ADD){
					Set<JChannelGroup> clients = get(metadata);
					clients.add(channelGroup);
				} else if(event == NotifyEvent.CHILD_REMOVED){
					removeChannelGroup(meta.getAddress());
				}
			}
		});
	}
	
	protected Set<JChannelGroup> get(ServiceMetadata metadata){
		Set<JChannelGroup> clients = clientMap.get(metadata);
		if(clients == null){
			Set<JChannelGroup> newClients = new HashSet<>();
			clients = clientMap.putIfAbsent(metadata, newClients);
			if(clients == null){
				clients = newClients;
			}
		}
		return clients;
	}
	
	public JChannelGroup group(Address address){
		JChannelGroup channelGroup = groups.get(address);
		if(channelGroup == null){
			JChannelGroup newChannelGroup = newChannelGroup(address);
			channelGroup = groups.putIfAbsent(address, newChannelGroup);
			if(channelGroup == null){
				channelGroup = newChannelGroup;
			}
		}
		return channelGroup;
	}
	
	protected void removeChannelGroup(Address address){
		JChannelGroup channelGroup = groups.remove(address);
		channelGroup.close();
	}
	
	protected JChannelGroup newChannelGroup(Address address){
		NettyChannelGroup channelGroup = new NettyChannelGroup();
		JChannel channel = connect(address.getHost(), address.getPort());
		channelGroup.bind(channel);
		return channelGroup;
	}
	
	protected abstract JChannel connect(String host, int port);

	private ServiceMeta asServiceMeta(ServiceMetadata metadata) {
		ServiceMeta meta = new RegisterMeta().getServiceMeta();
		meta.setGroup(metadata.getGroup());
		meta.setVersion(metadata.getVersion());
		meta.setServiceProviderName(metadata.getProviderName());
		return meta;
	}

	public JChannel select(ServiceMetadata metadata) {
		Set<JChannelGroup> groups = clientMap.get(metadata);
		JChannelGroup channelGroup = loadBalance.select(Arrays.asList(groups.toArray(new JChannelGroup[]{})));
		return channelGroup.channel();
	}
}
