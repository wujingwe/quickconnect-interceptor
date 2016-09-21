package me.oldjing.quickconnect.json;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}
		ServerInfoJson rhs = (ServerInfoJson) obj;
		return new EqualsBuilder()
				.append(sites, rhs.sites)
				.append(server, rhs.server)
				.append(env, rhs.env)
				.append(service, rhs.service)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(sites)
				.append(server)
				.append(env)
				.append(service)
				.toHashCode();
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
		public boolean equals(Object obj) {
			if (obj == null) { return false; }
			if (obj == this) { return true; }
			if (obj.getClass() != getClass()) {
				return false;
			}
			ServerJson rhs = (ServerJson) obj;
			return new EqualsBuilder()
					       .append(serverID, rhs.serverID)
					       .append(ddns, rhs.ddns)
					       .append(fqdn, rhs.fqdn)
					       .append(gateway, rhs.gateway)
					       .append(_interface, rhs._interface)
					       .append(external, rhs.external)
					       .isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					       .append(serverID)
					       .append(ddns)
					       .append(fqdn)
					       .append(gateway)
					       .append(_interface)
					       .append(external)
					       .toHashCode();
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
			public boolean equals(Object obj) {
				if (obj == null) { return false; }
				if (obj == this) { return true; }
				if (obj.getClass() != getClass()) {
					return false;
				}
				InterfaceJson rhs = (InterfaceJson) obj;
				return new EqualsBuilder()
						       .append(ip, rhs.ip)
						       .append(ipv6, rhs.ipv6)
						       .append(mask, rhs.mask)
						       .append(name, rhs.name)
						       .isEquals();
			}

			@Override
			public int hashCode() {
				return new HashCodeBuilder(17, 37)
						       .append(ip)
						       .append(ipv6)
						       .append(mask)
						       .append(name)
						       .toHashCode();
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
				public boolean equals(Object obj) {
					if (obj == null) { return false; }
					if (obj == this) { return true; }
					if (obj.getClass() != getClass()) {
						return false;
					}
					Ipv6Json rhs = (Ipv6Json) obj;
					return new EqualsBuilder()
							       .append(addr_type, rhs.addr_type)
							       .append(address, rhs.address)
							       .append(prefix_length, rhs.prefix_length)
							       .append(scope, rhs.scope)
							       .isEquals();
				}

				@Override
				public int hashCode() {
					return new HashCodeBuilder(17, 37)
							       .append(addr_type)
							       .append(address)
							       .append(prefix_length)
							       .append(scope)
							       .toHashCode();
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
			public boolean equals(Object obj) {
				if (obj == null) { return false; }
				if (obj == this) { return true; }
				if (obj.getClass() != getClass()) {
					return false;
				}
				ExternalJson rhs = (ExternalJson) obj;
				return new EqualsBuilder()
						       .append(ip, rhs.ip)
						       .append(ipv6, rhs.ipv6)
						       .isEquals();
			}

			@Override
			public int hashCode() {
				return new HashCodeBuilder(17, 37)
						       .append(ip)
						       .append(ipv6)
						       .toHashCode();
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
		public boolean equals(Object obj) {
			if (obj == null) { return false; }
			if (obj == this) { return true; }
			if (obj.getClass() != getClass()) {
				return false;
			}
			EnvJson rhs = (EnvJson) obj;
			return new EqualsBuilder()
					       .append(relay_region, rhs.relay_region)
					       .append(control_host, rhs.control_host)
					       .isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					       .append(relay_region)
					       .append(control_host)
					       .toHashCode();
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
		public boolean equals(Object obj) {
			if (obj == null) { return false; }
			if (obj == this) { return true; }
			if (obj.getClass() != getClass()) {
				return false;
			}
			ServiceJson rhs = (ServiceJson) obj;
			return new EqualsBuilder()
					       .append(port, rhs.port)
					       .append(ext_port, rhs.ext_port)
					       .append(relay_ip, rhs.relay_ip)
					       .append(relay_ipv6, rhs.relay_ipv6)
					       .append(relay_port, rhs.relay_port)
					       .isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					       .append(port)
					       .append(ext_port)
					       .append(relay_ip)
					       .append(relay_ipv6)
					       .append(relay_port)
					       .toHashCode();
		}
	}
}
