/**
 * 
 */
package org.jrpc.registry;

import java.util.Collection;

import org.jrpc.registry.RegisterMeta.ServiceMeta;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface Registry {
	
	void conncectServer(String serverList);

	void register(RegisterMeta meta);
	
	void unregister(RegisterMeta meta);
	
	void subscribe(ServiceMeta serviceMeta, NotifyListener listener);
	
	Collection<RegisterMeta> lookup(ServiceMeta serviceMeta, NotifyListener listener);
}
