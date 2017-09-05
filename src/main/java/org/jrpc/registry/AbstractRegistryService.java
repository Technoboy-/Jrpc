/**
 * 
 */
package org.jrpc.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jrpc.common.Pair;
import org.jrpc.registry.NotifyListener.NotifyEvent;
import org.jrpc.registry.RegisterMeta.ServiceMeta;

import com.sun.jndi.cosnaming.IiopUrl.Address;

import io.netty.util.internal.ConcurrentSet;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public abstract class AbstractRegistryService implements Registry{

	private final LinkedBlockingQueue<RegisterMeta> queues = new LinkedBlockingQueue<>(100);
	
	private final AtomicBoolean run = new AtomicBoolean(true);
	
	private final ConcurrentHashMap<ServiceMeta, CopyOnWriteArrayList<NotifyListener>> subscribeListeners = new ConcurrentHashMap<>();
	
	private final ConcurrentSet<RegisterMeta> registerMetaSet = new ConcurrentSet<>();
	 
	private final ConcurrentSet<ServiceMeta> subscribeSet = new ConcurrentSet<>();
	
	private final ConcurrentHashMap<ServiceMeta, Pair<Long, List<RegisterMeta>>> registries = new ConcurrentHashMap<>();
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private final ConcurrentHashMap<Address, CopyOnWriteArrayList<OfflineListener>> offlineListeners = new ConcurrentHashMap<>();
	
	private final Thread thread;
	
	public AbstractRegistryService(){
		thread = new Thread(new Runnable() {
			
			public void run() {
				while(run.get()){
					RegisterMeta meta = null;
					try {
						meta = queues.take();
						doRegister(meta);
					} catch (Exception e) {
						if (meta != null) {
							queues.add(meta);
	                    }
					}
				}
				
			}
		}, "registry.meta");
		thread.setDaemon(true);
		thread.start();
	}
	
	public void register(RegisterMeta meta){
		queues.add(meta);
	}
	
	public void unregister(RegisterMeta meta){
		doUnregister(meta);
	}
	
	public void subscribe(ServiceMeta serviceMeta, NotifyListener listener){
		CopyOnWriteArrayList<NotifyListener> listeners = subscribeListeners.get(serviceMeta);
		if(listeners == null){
			CopyOnWriteArrayList<NotifyListener> newListeners = new CopyOnWriteArrayList<NotifyListener>();
			listeners = subscribeListeners.putIfAbsent(serviceMeta, newListeners);
			if(listeners == null){
				listeners = newListeners;
			}
		}
		listeners.add(listener);
		doSubscribe(serviceMeta);
	}
	
	public void offline(Address address){
		CopyOnWriteArrayList<OfflineListener> listeners = offlineListeners.remove(address);
		if(listeners != null){
			for(OfflineListener listener : listeners){
				listener.offline();
			}
		}
	}
	
	protected void notify(ServiceMeta serviceMeta, RegisterMeta registerMeta, NotifyEvent event, long version){
		final Lock writeLock = lock.writeLock();
		boolean needNotify = false;
		writeLock.lock();
		try {
			Pair<Long, List<RegisterMeta>> pair = registries.get(serviceMeta);
			if(pair == null){
				if(event == NotifyEvent.CHILD_REMOVED){
					return;
				}
				List<RegisterMeta> metas = new ArrayList<>();
				metas.add(registerMeta);
				pair = new Pair<>(version, metas);
				needNotify = true;
			} else{
				long oldVersion = pair.getKey();
				List<RegisterMeta> values = pair.getValue();
				if(oldVersion < version || (version < 0 && oldVersion > 0)){
					if(event == NotifyEvent.CHILD_ADD){
						values.add(registerMeta);
					} else if(event == NotifyEvent.CHILD_REMOVED){
						values.remove(registerMeta);
					}
					pair = new Pair<Long, List<RegisterMeta>>(version, values);
					needNotify = true;
				}
			}
			registries.put(serviceMeta, pair);
		} finally {
			writeLock.unlock();
		}
		if(needNotify){
			CopyOnWriteArrayList<NotifyListener> listeners = subscribeListeners.get(serviceMeta);
			if(listeners != null){
				for(NotifyListener listener : listeners){
					listener.notify(registerMeta, event);
				}
			}
		}
	}
	
	public ConcurrentSet<RegisterMeta> getRegisterMetaSet() {
		return registerMetaSet;
	}
	
	public ConcurrentSet<ServiceMeta> getSubscribeSet() {
		return subscribeSet;
	}

	protected abstract void doSubscribe(ServiceMeta serviceMeta);

    protected abstract void doRegister(RegisterMeta meta);

    protected abstract void doUnregister(RegisterMeta meta);
	
	
}
