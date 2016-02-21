package me.oldjing.quickconnect.store;

public interface RelayStore {
	void add(String serverID, RelayCookie relayCookie);

	RelayCookie get(String serverID);

	void remove(String serverID);

	void removeAll();
}
