package me.oldjing.quickconnect.store;

import okhttp3.HttpUrl;

public class RelayCookie {

	private String serverID;
	private String id;
	private HttpUrl resolvedUrl;

	private RelayCookie(Builder builder) {
		serverID = builder.serverID;
		id = builder.id;
		resolvedUrl = builder.resolvedUrl;
	}

	public Builder newBuilder() {
		return new Builder()
				       .serverID(serverID)
				       .id(id)
				       .resolvedUrl(resolvedUrl);
	}

	public String serverID() {
		return serverID;
	}

	public String id() {
		return id;
	}

	public HttpUrl resolvedUrl() {
		return resolvedUrl;
	}

	public static class Builder {
		private String serverID;
		private String id; // dsm_portal or dsm_portal_https
		private HttpUrl resolvedUrl;

		public Builder serverID(String serverID) {
			this.serverID = serverID;
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder resolvedUrl(HttpUrl resolvedUrl) {
			this.resolvedUrl = resolvedUrl;
			return this;
		}

		public RelayCookie build() {
			return new RelayCookie(this);
		}
	}
}
