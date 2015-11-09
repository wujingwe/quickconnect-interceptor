package me.oldjing.quickconnect.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerInfoJson {
	public List<String> sites;
	public ServerJson server;
	public EnvJson env;
	public ServiceJson service;

	public class ServerJson {
		public String serverID;
		public String ddns;
		public String fqdn;
		public String gateway;
		@SerializedName("interface")
		public List<InterfaceJson> _interface;
		public ExternalJson external;

		public class InterfaceJson {
			public String ip;
			public List<Ipv6Json> ipv6;
			public String mask;
			public String name;

			public class Ipv6Json {
				public int addr_type;
				public String address;
				public int prefix_length;
				public String scope;
			}
		}

		public class ExternalJson {
			public String ip;
			public String ipv6;
		}
	}

	public class EnvJson {
		public String relay_region;
		public String control_host;
	}

	public class ServiceJson {
		public int port;
		public int ext_port;
		public String relay_ip;
		public String relay_ipv6;
		public int relay_port;
	}
}
