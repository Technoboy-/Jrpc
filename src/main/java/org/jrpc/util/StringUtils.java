/**
 * 
 */
package org.jrpc.util;

import static org.jrpc.common.JConstants.UTF8;

import java.nio.charset.Charset;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null ? true : str.length() == 0;
	}

	public static String newStringUtf8(final byte[] bytes) {
		return newString(bytes, UTF8);
	}

	public static byte[] getBytesUtf8(final String string) {
		return getBytes(string, UTF8);
	}
	
	public static String newString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }

	public static byte[] getBytes(final String string, final Charset charset) {
		if (string == null) {
			return null;
		}
		return string.getBytes(charset);
	}
	
	
}
