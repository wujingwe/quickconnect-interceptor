package me.oldjing.quickconnect.store;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRelayStore implements RelayStore {

	// In memory
	private Map<String, RelayCookie> allCookies = new HashMap<>();

	@Override
	public void add(String serverID, RelayCookie relayCookie) {
		allCookies.put(serverID, relayCookie);
	}

	@Override
	public RelayCookie get(String serverID) {
		return allCookies.get(serverID);
	}

	@Override
	public void remove(String serverID) {
		allCookies.remove(serverID);
	}

	@Override
	public void removeAll() {
		allCookies.clear();
	}
}
