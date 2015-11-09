package me.oldjing.quickconnect;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Util {
	public static boolean isQuickConnectId(String host) {
		return (host != null
				&& host.indexOf('.') < 0
				&& host.indexOf(':') < 0);
	}

	private static Map<String, Connection> sConnectionMap;

	static {
		sConnectionMap = new ConcurrentHashMap<>();
	}

	public static Connection addConnection(final String serverID, String id) {
		if (serverID == null) {
			throw new IllegalArgumentException("serverID == null");
		}
		if (id == null) {
			throw new IllegalArgumentException("id == null");
		}

		Connection connection = new Connection.Builder()
				.serverID(serverID).id(id).build();
		sConnectionMap.put(serverID, connection);
		return connection;
	}

	public static void setConnection(Connection connection) {
		if (connection == null) {
			throw new IllegalArgumentException("connection == null");
		}
		String serverID = connection.serverID();
		if (serverID == null) {
			throw new IllegalArgumentException("serverID == null");
		}
		sConnectionMap.put(serverID, connection);
	}

	public static Connection getConnection(String serverID) {
		if (serverID == null) {
			throw new IllegalArgumentException("serverID == null");
		}
		return sConnectionMap.get(serverID);
	}

	public static Map<String, Connection> getConnections() {
		return sConnectionMap;
	}

	public static void reset() {
		sConnectionMap.clear();
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
