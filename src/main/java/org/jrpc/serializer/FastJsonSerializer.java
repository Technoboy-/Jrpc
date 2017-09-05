/**
 * 
 */
package org.jrpc.serializer;

import static org.jrpc.common.JConstants.UTF8;

import com.alibaba.fastjson.JSON;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class FastJsonSerializer implements Serializer{

	public <T> byte[] serialize(T obj)  {
		String json = JSON.toJSONString(obj);
		return json.getBytes(UTF8);
	}

	public <T> T deserialize(byte[] src, Class<T> clazz) {
		return JSON.parseObject(new String(src, UTF8), clazz);
	}

	public static String getValue(String json, String field){
		return JSON.parseObject(json).getString(field);
	}
	
	public static <T> String serializeToString(T obj) {
		return JSON.toJSONString(obj);
	}
}
