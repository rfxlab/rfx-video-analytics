package rfxlab.com.demo.utils;

import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

import rfx.core.util.StringPool;
import rfx.core.util.StringUtil;

public class BeaconUtil {

	public static Map<String, List<String>> getQueryMap(String query) {
		QueryStringDecoder decoder = new QueryStringDecoder("?" + query);
		return decoder.parameters();
	}

	public static String getParam(Map<String, List<String>> params, String key, String defaultVal, String where) {
		if (params.containsKey(key)) {
			List<String> vals = params.get(key);
			if (vals != null) {
				if (!StringUtil.isEmpty(where)) {
					for (String val : vals) {
						if (val.contains(where)) {
							return val;
						}
					}
				} else {
					return vals.get(0);
				}

			}
		}
		return defaultVal;
	}

	public static String getParam(Map<String, List<String>> params, String key, String defaultVal, int index) {
		if (params.containsKey(key)) {
			List<String> vals = params.get(key);
			if (vals != null) {
				if (index >= 0 && index < vals.size()) {
					return StringUtil.decodeUrlUTF8(vals.get(index));
				}
			}
		}
		return defaultVal;
	}
	
	public static String getParam(Map<String, List<String>> params, String key) {
		return getParam(params, key, StringPool.BLANK);
	}

	public static String getParam(Map<String, List<String>> params, String key, String defaultVal) {		
		if (params.containsKey(key)) {
			List<String> vals = params.get(key);
			if (vals != null) {				
				for (String val : vals) {
					if (StringUtil.isNotEmpty(val)) {
						return val;
					}
				}
			}
		}
		return defaultVal;
	}

	public static String extractUrlValue(String tok) {
		try {
			tok = tok.substring(tok.indexOf("&url=http") + 5);
			String v = tok.substring(0, tok.indexOf("&"));
			return v;
		} catch (Throwable e) {
		}
		return StringPool.BLANK;
	}

	public static String extractUUID(String tok) {
		try {
			tok = tok.substring(tok.indexOf("apluuid=") + 9);
			int lastIndex = tok.indexOf(";");
			String v = lastIndex > 0 ? tok.substring(0, lastIndex) : tok;
			return v;
		} catch (Throwable e) {
		}
		return StringPool.BLANK;
	}
	
	public static String extractRefererURL(String cookie) {
		try {
			cookie = cookie.substring(cookie.indexOf("referer=") + 8);
			int lastIndex = cookie.indexOf(";");
			String v = lastIndex > 0 ? cookie.substring(0, lastIndex) : cookie;
			return v;
		} catch (Throwable e) {
		}
		return StringPool.BLANK;
	}

	
	public static String extractDomainFromUrl(String url) {
		try {
			url = url.substring(url.indexOf("://") + 3);
			String v = url.substring(0, url.indexOf("/"));
			if (!v.startsWith("="))
				return v;
		} catch (Throwable e) {
		}
		return StringPool.BLANK;
	}
			
}

