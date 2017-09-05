/**
 * 
 */
package org.jrpc.common;

import static org.jrpc.common.JConstants.DEFAULT_GROUP;
import static org.jrpc.common.JConstants.DEFAULT_VERSION;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caoguo(jiwei.guo)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceProvider {

	String value() default "";
	
	String group() default DEFAULT_GROUP;
	
	String version() default DEFAULT_VERSION;
}
