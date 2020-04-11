package me.oldjing.quickconnect.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerInfoJson {
	public List<String> sites;
	public ServerJson server;
	public EnvJson env;
	public ServiceJson service;

	public ServerInfoJson(List<String> sites, ServerJson server, EnvJson env, ServiceJson service) {
		this.sites = sites;
		this.server = server;
		this.env = env;
		this.service = service;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ServerInfoJson)) return false;

		ServerInfoJson that = (ServerInfoJson) o;

		if (sites != null ? !sites.equals(that.sites) : that.sites != null) return false;
		if (server != null ? !server.equals(that.server) : that.server != null) return false;
		if (env != null ? !env.equals(that.env) : that.env != null) return false;
		return service != null ? service.equals(that.service) : that.service == null;
	}

	@Override
	public int hashCode() {
		int result = sites != null ? sites.hashCode() : 0;
		result = 31 * result + (server != null ? server.hashCode() : 0);
		result = 31 * result + (env != null ? env.hashCode() : 0);
		result = 31 * result + (service != null ? service.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("ServerInfoJson{");
		sb.append("sites=").append(sites);
		sb.append(", server=").append(server);
		sb.append(", env=").append(env);
		sb.append(", service=").append(service);
		sb.append('}');
		return sb.toString();
	}

	public static class ServerJson {
		public String serverID;
		public String ddns;
		public String fqdn;
		public String gateway;
		@SerializedName("interface")
		public List<InterfaceJson> _interface;
		public ExternalJson external;

		public ServerJson(String serverID, String ddns, String fqdn, String gateway,
		                  List<InterfaceJson> _interface, ExternalJson external) {
			this.serverID = serverID;
			this.ddns = ddns;
			this.fqdn = fqdn;
			this.gateway = gateway;
			this._interface = _interface;
			this.external = external;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ServerJson)) return false;

			ServerJson that = (ServerJson) o;

			if (serverID != null ? !serverID.equals(that.serverID) : that.serverID != null)
				return false;
			if (ddns != null ? !ddns.equals(that.ddns) : that.ddns != null) return false;
			if (fqdn != null ? !fqdn.equals(that.fqdn) : that.fqdn != null) return false;
			if (gateway != null ? !gateway.equals(that.gateway) : that.gateway != null)
				return false;
			if (_interface != null ? !_interface.equals(that._interface) : that._interface != null)
				return false;
			return external != null ? external.equals(that.external) : that.external == null;
		}

		@Override
		public int hashCode() {
			int result = serverID != null ? serverID.hashCode() : 0;
			result = 31 * result + (ddns != null ? ddns.hashCode() : 0);
			result = 31 * result + (fqdn != null ? fqdn.hashCode() : 0);
			result = 31 * result + (gateway != null ? gateway.hashCode() : 0);
			result = 31 * result + (_interface != null ? _interface.hashCode() : 0);
			result = 31 * result + (external != null ? external.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("ServerJson{");
			sb.append("serverID='").append(serverID).append('\'');
			sb.append(", ddns='").append(ddns).append('\'');
			sb.append(", fqdn='").append(fqdn).append('\'');
			sb.append(", gateway='").append(gateway).append('\'');
			sb.append(", _interface=").append(_interface);
			sb.append(", external=").append(external);
			sb.append('}');
			return sb.toString();
		}

		public static class InterfaceJson {
			public String ip;
			public List<Ipv6Json> ipv6;
			public String mask;
			public String name;

			public InterfaceJson(String ip, List<Ipv6Json> ipv6, String mask, String name) {
				this.ip = ip;
				this.ipv6 = ipv6;
				this.mask = mask;
				this.name = name;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (!(o instanceof InterfaceJson)) return false;

				InterfaceJson that = (InterfaceJson) o;

				if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
				if (ipv6 != null ? !ipv6.equals(that.ipv6) : that.ipv6 != null) return false;
				if (mask != null ? !mask.equals(that.mask) : that.mask != null) return false;
				return name != null ? name.equals(that.name) : that.name == null;
			}

			@Override
			public int hashCode() {
				int result = ip != null ? ip.hashCode() : 0;
				result = 31 * result + (ipv6 != null ? ipv6.hashCode() : 0);
				result = 31 * result + (mask != null ? mask.hashCode() : 0);
				result = 31 * result + (name != null ? name.hashCode() : 0);
				return result;
			}

			@Override
			public String toString() {
				final StringBuffer sb = new StringBuffer("InterfaceJson{");
				sb.append("ip='").append(ip).append('\'');
				sb.append(", ipv6=").append(ipv6);
				sb.append(", mask='").append(mask).append('\'');
				sb.append(", name='").append(name).append('\'');
				sb.append('}');
				return sb.toString();
			}

			public static class Ipv6Json {
				public int addr_type;
				public String address;
				public int prefix_length;
				public String scope;

				public Ipv6Json(int addr_type, String address, int prefix_length, String scope) {
					this.addr_type = addr_type;
					this.address = address;
					this.prefix_length = prefix_length;
					this.scope = scope;
				}

				@Override
				public boolean equals(Object o) {
					if (this == o) return true;
					if (!(o instanceof Ipv6Json)) return false;

					Ipv6Json ipv6Json = (Ipv6Json) o;

					if (addr_type != ipv6Json.addr_type) return false;
					if (prefix_length != ipv6Json.prefix_length) return false;
					if (address != null ? !address.equals(ipv6Json.address) : ipv6Json.address != null)
						return false;
					return scope != null ? scope.equals(ipv6Json.scope) : ipv6Json.scope == null;
				}

				@Override
				public int hashCode() {
					int result = addr_type;
					result = 31 * result + (address != null ? address.hashCode() : 0);
					result = 31 * result + prefix_length;
					result = 31 * result + (scope != null ? scope.hashCode() : 0);
					return result;
				}

				@Override
				public String toString() {
					final StringBuffer sb = new StringBuffer("Ipv6Json{");
					sb.append("addr_type=").append(addr_type);
					sb.append(", address='").append(address).append('\'');
					sb.append(", prefix_length=").append(prefix_length);
					sb.append(", scope='").append(scope).append('\'');
					sb.append('}');
					return sb.toString();
				}
			}
		}

		public static class ExternalJson {
			public String ip;
			public String ipv6;

			public ExternalJson(String ip, String ipv6) {
				this.ip = ip;
				this.ipv6 = ipv6;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (!(o instanceof ExternalJson)) return false;

				ExternalJson that = (ExternalJson) o;

				if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
				return ipv6 != null ? ipv6.equals(that.ipv6) : that.ipv6 == null;
			}

			@Override
			public int hashCode() {
				int result = ip != null ? ip.hashCode() : 0;
				result = 31 * result + (ipv6 != null ? ipv6.hashCode() : 0);
				return result;
			}

			@Override
			public String toString() {
				final StringBuffer sb = new StringBuffer("ExternalJson{");
				sb.append("ip='").append(ip).append('\'');
				sb.append(", ipv6='").append(ipv6).append('\'');
				sb.append('}');
				return sb.toString();
			}
		}
	}

	public static class EnvJson {
		public String relay_region;
		public String control_host;

		public EnvJson(String relay_region, String control_host) {
			this.relay_region = relay_region;
			this.control_host = control_host;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof EnvJson)) return false;

			EnvJson envJson = (EnvJson) o;

			if (relay_region != null ? !relay_region.equals(envJson.relay_region) : envJson.relay_region != null)
				return false;
			return control_host != null ? control_host.equals(envJson.control_host) : envJson.control_host == null;
		}

		@Override
		public int hashCode() {
			int result = relay_region != null ? relay_region.hashCode() : 0;
			result = 31 * result + (control_host != null ? control_host.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("EnvJson{");
			sb.append("relay_region='").append(relay_region).append('\'');
			sb.append(", control_host='").append(control_host).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}

	public static class ServiceJson {
		public int port;
		public int ext_port;
		public String relay_ip;
		public String relay_ipv6;
		public int relay_port;

		public ServiceJson(int port, int ext_port, String relay_ip, String relay_ipv6, int relay_port) {
			this.port = port;
			this.ext_port = ext_port;
			this.relay_ip = relay_ip;
			this.relay_ipv6 = relay_ipv6;
			this.relay_port = relay_port;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof ServiceJson)) return false;

			ServiceJson that = (ServiceJson) o;

			if (port != that.port) return false;
			if (ext_port != that.ext_port) return false;
			if (relay_port != that.relay_port) return false;
			if (relay_ip != null ? !relay_ip.equals(that.relay_ip) : that.relay_ip != null)
				return false;
			return relay_ipv6 != null ? relay_ipv6.equals(that.relay_ipv6) : that.relay_ipv6 == null;
		}

		@Override
		public int hashCode() {
			int result = port;
			result = 31 * result + ext_port;
			result = 31 * result + (relay_ip != null ? relay_ip.hashCode() : 0);
			result = 31 * result + (relay_ipv6 != null ? relay_ipv6.hashCode() : 0);
			result = 31 * result + relay_port;
			return result;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("ServiceJson{");
			sb.append("port=").append(port);
			sb.append(", ext_port=").append(ext_port);
			sb.append(", relay_ip='").append(relay_ip).append('\'');
			sb.append(", relay_ipv6='").append(relay_ipv6).append('\'');
			sb.append(", relay_port=").append(relay_port);
			sb.append('}');
			return sb.toString();
		}
	}
}
