package me.oldjing.quickconnect.store;

public class RelayManager extends RelayHandler {

	private RelayStore relayJar = null;

	public RelayManager() {
		this(null);
	}

	public RelayManager(RelayStore store) {
		if (store == null) {
			relayJar = new InMemoryRelayStore();
		} else {
			relayJar = store;
		}
	}

	@Override
	public RelayCookie get(String serverID, int port) {
		return relayJar.get(serverID, port);
	}

	@Override
	public void put(String serverID, int port, RelayCookie cookie) {
		relayJar.add(serverID, port, cookie);
	}

	@Override
	public void remove(String serverID, int port) {
		relayJar.remove(serverID, port);
	}

	@Override
	public void removeAll() {
		relayJar.removeAll();
	}
}
