package me.oldjing.quickconnect;

import java.util.Collection;

public class Util {
	public static boolean isQuickConnectId(String host) {
		return (host != null
				&& host.indexOf('.') < 0
				&& host.indexOf(':') < 0);
	}

	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		if (object instanceof String) {
			return ((String) object).length() == 0;
		} else if (object instanceof Collection) {
			return ((Collection) object).isEmpty();
		} else if (object instanceof Object[]) {
			return ((Object[]) object).length == 0;
		}
		return true;
	}
}
