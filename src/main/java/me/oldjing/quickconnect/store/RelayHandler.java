package me.oldjing.quickconnect.store;

public abstract class RelayHandler {
	private static RelayHandler sInstance;

	public static RelayHandler getDefault() {
		return sInstance;
	}

	public static void setDefault(RelayHandler relayHandler) {
		sInstance = relayHandler;
	}

	public abstract RelayCookie get(String serverID);

	public abstract void put(String serverID, RelayCookie cookie);

	public abstract void remove(String serverID);

	public abstract void removeAll();
}
