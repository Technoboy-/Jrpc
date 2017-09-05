package org.jrpc.serializer;

import org.jrpc.serializer.extension.SPILoader;

public class SerializerImpl {
	
	public static Serializer serializerImpl(){
		return SPILoader.getSPIClass(Serializer.class).getExtension();
	}
}
