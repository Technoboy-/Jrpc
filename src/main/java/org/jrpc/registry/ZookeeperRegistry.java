/**
 * 
 */
package org.jrpc.registry;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.jrpc.registry.NotifyListener.NotifyEvent;
import org.jrpc.registry.RegisterMeta.Address;
import org.jrpc.registry.RegisterMeta.ServiceMeta;
import org.jrpc.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.ConcurrentSet;



/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ZookeeperRegistry extends AbstractRegistryService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);
	
	private CuratorFramework client;
	
	private final int sessionTimeoutMs = Integer.valueOf(System.getProperty("jrpc.registry.zookeeper.sessionTimeoutMs", "60000"));
    private final int connectionTimeoutMs = Integer.valueOf(System.getProperty("jrpc.registry.zookeeper.connectionTimeoutMs", "15000"));
	
	private final ConcurrentHashMap<ServiceMeta, PathChildrenCache> pathChildrenCaches = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<Address, ConcurrentSet<ServiceMeta>> serviceMetaMap = new ConcurrentHashMap<>();
	
	private final AtomicLong version = new AtomicLong(0);
	

	public Collection<RegisterMeta> lookup(ServiceMeta serviceMeta, NotifyListener listener) {
		String directory = String.format("/jrpc/provider/%s/%s/%s", serviceMeta.getGroup(), serviceMeta.getVersion(), serviceMeta.getServiceProviderName());
		List<RegisterMeta> metas = new ArrayList<>();
		try {
			List<String> paths = client.getChildren().forPath(directory);
			for(String path : paths){
				metas.add(parseRegisterMeta(String.format("%s/%s", directory, path)));
			}
		} catch (Exception e) {
			LOGGER.error("lookup service meta : {}, failed, {}", serviceMeta, e);
		}
		return metas;
	}
	
	private RegisterMeta parseRegisterMeta(String data){
		String[] array_0 = data.split("/");
		RegisterMeta meta = new RegisterMeta();
        meta.setGroup(array_0[3]);
        meta.setVersion(array_0[4]);
        meta.setServiceProviderName(array_0[5]);
        
        String[] array_1 = array_0[6].split(":");
        meta.setHost(array_1[0]);
        meta.setPort(Integer.parseInt(array_1[1]));
        
        return meta;
		
	}

	protected void doSubscribe(ServiceMeta serviceMeta) {
		PathChildrenCache childrenCache = pathChildrenCaches.get(serviceMeta);
		if(childrenCache == null){
			String directory = String.format("/jprc/provider/%s/%s/%s", serviceMeta.getGroup(), serviceMeta.getVersion(), serviceMeta.getServiceProviderName());
			PathChildrenCache newPathChildrenCache = new PathChildrenCache(client, directory, false);
			childrenCache = pathChildrenCaches.putIfAbsent(serviceMeta, newPathChildrenCache);
            if (childrenCache == null) {
                childrenCache = newPathChildrenCache;
                childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
					
					@Override
					public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
						LOGGER.info("child event : {}", event);
						switch(event.getType()){
							case CHILD_ADDED:{
								
								RegisterMeta meta = parseRegisterMeta(event.getData().getPath());
								Address address = meta.getAddress();
								ServiceMeta serviceMeta = meta.getServiceMeta();
								ConcurrentSet<ServiceMeta> serviceMetaSet = getServiceMeta(address);
								
								serviceMetaSet.add(serviceMeta);
								
								ZookeeperRegistry.this.notify(serviceMeta, meta, NotifyEvent.CHILD_ADD, version.getAndIncrement());
								break;
							}
							case CHILD_REMOVED:{
								RegisterMeta meta = parseRegisterMeta(event.getData().getPath());
								Address address = meta.getAddress();
								ServiceMeta serviceMeta = meta.getServiceMeta();
								ConcurrentSet<ServiceMeta> serviceMetaSet = getServiceMeta(address);
								
								serviceMetaSet.remove(serviceMeta);
								ZookeeperRegistry.this.notify(serviceMeta, meta, NotifyEvent.CHILD_REMOVED, version.getAndIncrement());
								if(serviceMetaSet.isEmpty()){
									//ZookeeperRegistry.this.offline(address);
								}
								break;
							}
						}
						
					}
				});
            }
            try {
                childrenCache.start();
            } catch (Exception e) {
            	LOGGER.warn("Subscribe {} failed, {}.", directory, e);
            }
		}
	}
	
	private ConcurrentSet<ServiceMeta> getServiceMeta(Address address) {
		ConcurrentSet<ServiceMeta> serviceMetaSet = serviceMetaMap.get(address);
        if (serviceMetaSet == null) {
            ConcurrentSet<ServiceMeta> newServiceMetaSet = new ConcurrentSet<>();
            serviceMetaSet = serviceMetaMap.putIfAbsent(address, newServiceMetaSet);
            if (serviceMetaSet == null) {
                serviceMetaSet = newServiceMetaSet;
            }
        }
        return serviceMetaSet;
	}

	protected void doRegister(RegisterMeta meta) {
		String directory = String.format("/jprc/provider/%s/%s/%s", meta.getGroup(), meta.getVersion(), meta.getServiceProviderName());
		try {
			if(client.checkExists().forPath(directory) == null){
				client.create().creatingParentsIfNeeded().forPath(directory);
			}
		} catch (Exception e) {
			LOGGER.warn("doRegister {} failed, {}.", directory, e);
		}
		meta.setHost(NetUtils.getLocalIp());
		try {
			client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
				
				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					getRegisterMetaSet().add(meta);
					
					LOGGER.info("Register: {}.", meta);
				}
			}).forPath(String.format("%s/%s:%s", directory, meta.getHost(), String.valueOf(meta.getPort())));
		} catch (Exception e) {
			LOGGER.warn("doRegister {} failed, {}.", directory, e);
		}
	}

	protected void doUnregister(RegisterMeta meta) {
		String directory = String.format("/jupiter/provider/%s/%s/%s",
                meta.getGroup(),
                meta.getVersion(),
                meta.getServiceProviderName());
		 try {
	            if (client.checkExists().forPath(directory) == null) {
	                return;
	            }
	        } catch (Exception e) {
	        	LOGGER.warn("Check exists with parent path failed, directory: {}, {}.", directory, e);
	        }

	        try {
	            meta.setHost(NetUtils.getLocalIp());

	            client.delete().inBackground(new BackgroundCallback() {

	                @Override
	                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
	                	getRegisterMetaSet().remove(meta);

	                	LOGGER.info("Unregister: {}.", meta);
	                }
	            }).forPath(
	                    String.format("%s/%s:%s",
	                            directory,
	                            meta.getHost(),
	                            String.valueOf(meta.getPort())));
	        } catch (Exception e) {
	        	LOGGER.warn("Delete register meta: {} path failed, {}.", meta, e);
	        }
	}

	public void conncectServer(String serverList) {
		client = CuratorFrameworkFactory.newClient(serverList, sessionTimeoutMs, connectionTimeoutMs, new ExponentialBackoffRetry(1000, 3));
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            	LOGGER.info("Zookeeper connection state changed {}.", newState);

                if (newState == ConnectionState.RECONNECTED) {

                	LOGGER.info("Zookeeper connection has been re-established, will re-subscribe and re-register.");

                    // 重新订阅
                    for (ServiceMeta serviceMeta : getSubscribeSet()) {
                        doSubscribe(serviceMeta);
                    }

                    // 重新发布服务
                    for (RegisterMeta meta : getRegisterMetaSet()) {
                        doRegister(meta);
                    }
                }
            }
        });
		client.start();
	}
}

