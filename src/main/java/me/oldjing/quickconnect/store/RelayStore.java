package me.oldjing.quickconnect.store;

public interface RelayStore {
	void add(String serverID, int port, RelayCookie relayCookie);

	RelayCookie get(String serverID, int port);

	void remove(String serverID, int port);

	void removeAll();
}
