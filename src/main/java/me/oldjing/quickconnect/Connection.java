package me.oldjing.quickconnect;

import com.squareup.okhttp.HttpUrl;

import java.io.Serializable;

public class Connection implements Serializable {
	public static final String ID_DSM_PORTAL = "dsm_portal";
	public static final String ID_DSM_PORTAL_HTTPS = "dsm_portal_https";

	private String serverID;
	private String id; // dsm_portal or dsm_portal_https
	private HttpUrl resolvedUrl;

	public Connection(Builder builder) {
		serverID = builder.serverID;
		id = builder.id;
		resolvedUrl = builder.resolvedUrl;
	}

	private Connection() {}

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

		public Connection build() {
			return new Connection(this);
		}
	}
}
