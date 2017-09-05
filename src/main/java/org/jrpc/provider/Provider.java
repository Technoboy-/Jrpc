/**
 * 
 */
package org.jrpc.provider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jrpc.common.ServiceProvider;
import org.jrpc.consumer.MessageWrapper;
import org.jrpc.registry.RegisterMeta;
import org.jrpc.registry.Registry;
import org.jrpc.registry.ZookeeperRegistry;
import org.jrpc.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public class Provider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);

	private static final Map<String, Object> beans = new HashMap<>();
	
	private final Registry registry = new ZookeeperRegistry();
	
	private final JConfig config;
	
	public Provider(JConfig config){
		this.config = config;
		registry.conncectServer(config.getZkServerList());
	}
	
	public void register(Class<?> interfaceType, Object impl){
		if(!interfaceType.isInterface()){
			throw new RuntimeException("service " + interfaceType + " is not interface ");
		}
		if(!interfaceType.isAssignableFrom(impl.getClass())){
			throw new RuntimeException("impl " + impl + " is not assignableFrom service " + interfaceType);
		}
		ServiceProvider annotation = interfaceType.getAnnotation(ServiceProvider.class);
		String serviceProviderName = annotation.value();
		if(StringUtils.isEmpty(serviceProviderName)){
			serviceProviderName = interfaceType.getName();
		}
		RegisterMeta meta = new RegisterMeta();
		meta.setPort(config.getPort());
		meta.setVersion(annotation.version());
		meta.setGroup(annotation.group());
		meta.setServiceProviderName(serviceProviderName);
		registry.register(meta);
		LOGGER.debug("register meta : " + meta);
		beans.put(interfaceType.getName(), impl);
	}
	
	public static Object invoke(MessageWrapper message){
		Object target = beans.get(message.getMetadata().getProviderName());
		Object result = null;
		try {
			Method method = null;
			Class<?>[] args = new Class<?>[message.getArgs().length];
			for(int i = 0; i < message.getArgs().length; i++){
				args[i] = message.getArgs()[i] != null ? message.getArgs()[i].getClass() : Object.class;
			}
			method = target.getClass().getMethod(message.getMethodName(), args);
			result = method.invoke(target, message.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
