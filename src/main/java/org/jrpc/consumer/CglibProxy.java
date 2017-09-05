/**
 * 
 */
package org.jrpc.consumer;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


/**
 * @author caoguo(jiwei.guo)
 *
 */
public class CglibProxy{
	
    public <T> T newProxy(Class<T> interfaceType, Object handler){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceType);
        enhancer.setClassLoader(interfaceType.getClassLoader());
        enhancer.setCallback((MethodInterceptor)handler);
        return interfaceType.cast(enhancer.create());
    }
}
