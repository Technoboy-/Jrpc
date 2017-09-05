/**
 * 
 */
package org.jrpc.serializer;

import org.jrpc.serializer.extension.SPILoader;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public enum SerializerFactory {
	
	FASTJSON(SPILoader.getSPIClass(Serializer.class).getExtension("fastjson")),
	
	ProtoStuff(SPILoader.getSPIClass(Serializer.class).getExtension("protostuff"));
	
	private final Serializer delegate;
	
	private SerializerFactory(Serializer instance){
		this.delegate = instance;
	}

	public <T> byte[] serialize(T obj) {
		return delegate.serialize(obj);
	}
	
	public <T> T deserialize(byte[] src, Class<T> clazz) {
		return delegate.deserialize(src, clazz);
	}
}
