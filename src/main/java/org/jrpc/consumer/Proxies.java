package org.jrpc.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import static org.jrpc.util.Preconditions.checkArgument;

/**
 * 
 * @author tboy(jiwei.guo)
 *
 */
public enum Proxies {
    JDK_PROXY(new ProxyDelegate() {

        public <T> T newProxy(Class<T> interfaceType, Object handler) {
            checkArgument(handler instanceof InvocationHandler, "handler must be a InvocationHandler");

            Object object = Proxy.newProxyInstance(
                    interfaceType.getClassLoader(), new Class<?>[] { interfaceType }, (InvocationHandler) handler);
            return interfaceType.cast(object);
        }
    }),
    CG_LIB(new ProxyDelegate() {

        public <T> T newProxy(Class<T> interfaceType, Object handler) {
            checkArgument(handler instanceof MethodInterceptor, "handler must be a MethodInterceptor");

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(interfaceType);
            enhancer.setCallback((MethodInterceptor) handler);
            enhancer.setClassLoader(interfaceType.getClassLoader());
            return interfaceType.cast(enhancer.create());
        }
    });

    private final ProxyDelegate delegate;

    Proxies(ProxyDelegate delegate) {
        this.delegate = delegate;
    }

    public static Proxies getDefault() {
        return CG_LIB;
    }

    public <T> T newProxy(Class<T> interfaceType, Object handler) {
        return delegate.newProxy(interfaceType, handler);
    }

    interface ProxyDelegate {

        <T> T newProxy(Class<T> interfaceType, Object handler);
    }
}
