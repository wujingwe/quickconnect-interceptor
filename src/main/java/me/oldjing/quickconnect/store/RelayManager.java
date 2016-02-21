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
	public RelayCookie get(String serverID) {
		return relayJar.get(serverID);
	}

	@Override
	public void put(String serverID, RelayCookie cookie) {
		relayJar.add(serverID, cookie);
	}

	@Override
	public void remove(String serverID) {
		relayJar.remove(serverID);
	}

	@Override
	public void removeAll() {
		relayJar.removeAll();
	}
}
