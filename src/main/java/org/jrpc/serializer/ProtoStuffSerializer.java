/**
 * 
 */
package org.jrpc.serializer;

import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * protostuff的实现, 最快，最小
 * 
 * @author caoguo(jiwei.guo)
 *
 */
@SuppressWarnings("unchecked")
public class ProtoStuffSerializer implements Serializer {

	private final ConcurrentHashMap<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<Class<?>, Schema<?>>();

	private final ThreadLocal<LinkedBuffer> bufThreadLocal = new ThreadLocal<LinkedBuffer>() {

		@Override
		protected LinkedBuffer initialValue() {
			return LinkedBuffer.allocate();
		}
	};

	public <T> byte[] serialize(T obj)  {
		Schema<T> schema = getSchema((Class<T>) obj.getClass());
		LinkedBuffer buf = bufThreadLocal.get();
		try {
			return ProtostuffIOUtil.toByteArray(obj, schema, buf);
		} finally {
			buf.clear();
		}
	}

	public <T> T deserialize(byte[] src, Class<T> clazz) {
		T msg = null;
		try {
			msg = clazz.newInstance();
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(src, msg, schema);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
        return msg;
	}

	private <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
		if (schema == null) {
			Schema<T> newSchema = RuntimeSchema.createFrom(clazz);
			schema = (Schema<T>) schemaCache.putIfAbsent(clazz, newSchema);
			if (schema == null) {
				schema = newSchema;
			}
		}
		return schema;
	}
}
