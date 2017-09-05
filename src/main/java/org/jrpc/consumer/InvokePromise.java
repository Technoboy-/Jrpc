/**
 * 
 */
package org.jrpc.consumer;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public abstract class InvokePromise<T> {

	public abstract Object getResult() throws Throwable;

    @SuppressWarnings("unchecked")
    public T get() throws  Throwable{
        T result;
        try {
            result = (T) getResult();
        } catch (Exception e) {
            throw e;
        } 
        return result;
    }
}
