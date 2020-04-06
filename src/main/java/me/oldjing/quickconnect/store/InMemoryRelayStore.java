package me.oldjing.quickconnect.store;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRelayStore implements RelayStore {

	// In memory
	private Map<String, RelayCookie> allCookies = new HashMap<>();

	@Override
	public void add(String serverID, int port, RelayCookie relayCookie) {
		allCookies.put(createKey(serverID, port), relayCookie);
	}

	@Override
	public RelayCookie get(String serverID, int port) {
		return allCookies.get(createKey(serverID, port));
	}

	@Override
	public void remove(String serverID, int port) {
		allCookies.remove(createKey(serverID, port));
	}

	@Override
	public void removeAll() {
		allCookies.clear();
	}

	private String createKey(String serverID, int port) {
		return String.format("%s_%s", serverID, port);
	}
}
