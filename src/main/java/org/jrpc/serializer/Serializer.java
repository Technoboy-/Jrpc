/**
 * 
 */
package org.jrpc.serializer;

import org.jrpc.serializer.extension.SPI;

/**
 * @author caoguo(jiwei.guo)
 *	
 */
@SPI("fastjson")
public interface Serializer {
	
	public <T> byte[] serialize(T obj) ;
	
	public <T> T deserialize(byte[] src, Class<T> clazz);
}
