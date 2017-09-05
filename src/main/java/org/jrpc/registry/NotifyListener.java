/**
 * 
 */
package org.jrpc.registry;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public interface NotifyListener{

	void notify(RegisterMeta meta, NotifyEvent event);
	
	enum NotifyEvent{
		CHILD_ADD,
		CHILD_REMOVED
	}
}
