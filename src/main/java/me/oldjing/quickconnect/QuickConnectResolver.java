package me.oldjing.quickconnect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import me.oldjing.quickconnect.json.PingPongJson;
import me.oldjing.quickconnect.json.ServerInfoJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson.InterfaceJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson.InterfaceJson.Ipv6Json;
import me.oldjing.quickconnect.json.ServerInfoJson.ServiceJson;
import me.oldjing.quickconnect.store.RelayCookie;
import me.oldjing.quickconnect.store.RelayHandler;
import me.oldjing.quickconnect.store.RelayManager;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickConnectResolver {
	private OkHttpClient defaultClient;
	private HttpUrl requestUrl;
	private Gson gson;

	private List<HttpUrl> availableServers = new ArrayList<>();
	private List<HttpUrl> checkedServers = new ArrayList<>();

	public QuickConnectResolver(HttpUrl requestUrl) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		try {
			SSLContext context = SSLContext.getInstance("TLS");
			TrustManager[] trustManagers = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
						throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			}};
			context.init(null, trustManagers, new SecureRandom());
			builder.sslSocketFactory(context.getSocketFactory());
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					// since most DSM doesn't have valid certificate, ignore verifying hostname
					return true;
				}
			});
		} catch (NoSuchAlgorithmException | KeyManagementException ignored) {}

		this.defaultClient = builder.build();
		this.requestUrl = requestUrl;
		this.gson = new Gson();
	}

	public RelayCookie resolve(String serverID, String id) throws IOException {
		if (!Util.isQuickConnectId(serverID)) {
			throw new IllegalArgumentException("serverID isn't a Quick Connect ID");
		}

		RelayManager relayManager = (RelayManager) RelayHandler.getDefault();
		RelayCookie cookie = relayManager.get(serverID);
		if (cookie == null) {
			cookie = new RelayCookie.Builder()
					.serverID(serverID)
					.id(id)
					.build();
			relayManager.put(serverID, cookie);
		}

		HttpUrl serverUrl = HttpUrl.parse("http://global.quickconnect.to/Serv.php");
		ServerInfoJson infoJson = getServerInfo(serverUrl, serverID, id);

		// ping DSM directly
		HttpUrl resolvedUrl = pingDSM(infoJson);
		if (resolvedUrl != null) {
			cookie = cookie.newBuilder()
					.resolvedUrl(resolvedUrl)
					.build();
			return cookie;
		}

		// ping DSM through tunnel
		resolvedUrl = pingTunnel(infoJson.service);
		if (resolvedUrl != null) {
			cookie = cookie.newBuilder()
					.resolvedUrl(resolvedUrl)
					.build();
			return cookie;
		}

		// request tunnel
		infoJson = requestTunnel(infoJson, serverID, id);
		if (infoJson != null) {
			resolvedUrl = requestUrl.newBuilder()
					.host(infoJson.service.relay_ip)
					.port(infoJson.service.relay_port)
					.build();
			cookie = cookie.newBuilder()
					.resolvedUrl(resolvedUrl)
					.build();
			return cookie;
		}

		throw new IOException("No valid url resolved");
	}

	public ServerInfoJson getServerInfo(HttpUrl serverUrl, String serverID, String id) throws IOException {
		availableServers.remove(serverUrl);
		checkedServers.add(serverUrl);
		// set timeout to 30 seconds
		OkHttpClient client = defaultClient.newBuilder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("version", 1);
		jsonObject.addProperty("command", "get_server_info");
		jsonObject.addProperty("stop_when_error", "false");
		jsonObject.addProperty("stop_when_success", "false");
		jsonObject.addProperty("id", id);
		jsonObject.addProperty("serverID", serverID);
		RequestBody requestBody = RequestBody.create(
				MediaType.parse("text/plain"), gson.toJson(jsonObject));
		Request request = new Request.Builder()
				.url(serverUrl)
				.post(requestBody)
				.build();
		Response response = client.newCall(request).execute();
		InputStream in = response.body().byteStream();
		JsonReader reader = null;
		ServerInfoJson serverInfoJson = null;
		try {
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			serverInfoJson = gson.fromJson(reader, ServerInfoJson.class);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		if (serverInfoJson != null) {
			if (serverInfoJson.server != null) {
				// server info found!
				return serverInfoJson;
			}

			if (serverInfoJson.sites != null && !serverInfoJson.sites.isEmpty()) {
				for (String site: serverInfoJson.sites) {
					HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("http").host(site).addPathSegment("Serv.php").build();
					if (!checkedServers.contains(httpUrl) && !availableServers.contains(httpUrl))
						availableServers.add(httpUrl);
				}
			}

			if (availableServers.size() > 0) {
				return getServerInfo(availableServers.remove(0), serverID, id);
			}
		}

		throw new IOException("No server info found!");
	}

	public HttpUrl pingDSM(ServerInfoJson infoJson) {
		// set timeout to 5 seconds
		final OkHttpClient client = defaultClient.newBuilder()
				.connectTimeout(5, TimeUnit.SECONDS)
				.readTimeout(5, TimeUnit.SECONDS)
				.build();

		ServerJson serverJson = infoJson.server;
		if (serverJson == null) {
			throw new IllegalArgumentException("serverJson == null");
		}
		ServiceJson serviceJson = infoJson.service;
		if (serviceJson == null) {
			throw new IllegalArgumentException("serviceJson == null");
		}
		int port = serviceJson.port;
		int externalPort = serviceJson.ext_port;

		// internal address(192.168.x.x/10.x.x.x)
		ExecutorService executor = Executors.newFixedThreadPool(10);
		CompletionService<String> internalService = new ExecutorCompletionService<>(executor);
		List<InterfaceJson> ifaces = serverJson._interface;
		AtomicInteger internalCount = new AtomicInteger(0);
		if (ifaces != null) {
			for (final InterfaceJson iface : ifaces) {
				internalService.submit(createPingTask(client, iface.ip, port));
				internalCount.incrementAndGet();

				if (iface.ipv6 != null) {
					for (Ipv6Json ipv6 : iface.ipv6) {
						String ipv6Address = "[" + ipv6.address + "]";
						internalService.submit(createPingTask(client, ipv6Address, port));
						internalCount.incrementAndGet();
					}
				}
			}
		}

		// host address(ddns/fqdn)
		ExecutorCompletionService<String> hostService = new ExecutorCompletionService<>(executor);
		AtomicInteger hostCount = new AtomicInteger(0);
		// ddns
		if (!Util.isEmpty(serverJson.ddns) && !serverJson.ddns.equals("NULL")) {
			hostService.submit(createPingTask(client, serverJson.ddns, port));
			hostCount.incrementAndGet();
		}
		// fqdn
		if (!Util.isEmpty(serverJson.fqdn) && !serverJson.fqdn.equals("NULL")) {
			hostService.submit(createPingTask(client, serverJson.fqdn, port));
			hostCount.incrementAndGet();
		}

		// external address(public ip address)
		ExecutorCompletionService<String> externalService = new ExecutorCompletionService<>(executor);
		AtomicInteger externalCount = new AtomicInteger(0);
		if (serverJson.external != null) {
			String ip = serverJson.external.ip;
			if (!Util.isEmpty(ip)) {
				externalService.submit(createPingTask(client, ip, (externalPort != 0) ? externalPort : port));
				externalCount.incrementAndGet();
			}
			String ipv6 = serverJson.external.ipv6;
			if (!Util.isEmpty(ipv6) && !ipv6.equals("::")) {
				externalService.submit(createPingTask(client, "[" + ipv6 + "]", (externalPort != 0) ? externalPort : port));
				externalCount.incrementAndGet();
			}
		}

		while (internalCount.getAndDecrement() > 0) {
			try {
				Future<String> future = internalService.take();
				if (future != null) {
					String host = future.get();
					if (!Util.isEmpty(host)) {
						return requestUrl.newBuilder().host(host).port(port).build();
					}
				}
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}

		while (hostCount.getAndDecrement() > 0) {
			try {
				Future<String> future = hostService.take();
				if (future != null) {
					String host = future.get();
					if (!Util.isEmpty(host)) {
						return requestUrl.newBuilder().host(host).port(port).build();
					}
				}
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}

		while (externalCount.getAndDecrement() > 0) {
			try {
				Future<String> future = externalService.take();
				if (future != null) {
					String host = future.get();
					if (!Util.isEmpty(host)) {
						return requestUrl.newBuilder().host(host).port(port).build();
					}
				}
			} catch (InterruptedException | ExecutionException ignored) {
//				ignored.printStackTrace();
			}
		}

		// shutdown executors
		executor.shutdownNow();

		return null;
	}

	private HttpUrl pingTunnel(ServiceJson serviceJson) {
		if (serviceJson == null
				|| Util.isEmpty(serviceJson.relay_ip)
				|| serviceJson.relay_port == 0) {
			return null;
		}

		// set timeout to 10 seconds
		OkHttpClient client = defaultClient.newBuilder()
				.connectTimeout(5, TimeUnit.SECONDS)
				.readTimeout(5, TimeUnit.SECONDS)
				.build();

		String relayIp = serviceJson.relay_ip;
		int relayPort = serviceJson.relay_port;

		// tunnel address
		ExecutorService executor = Executors.newFixedThreadPool(10);
		CompletionService<String> service = new ExecutorCompletionService<>(executor);
		service.submit(createPingTask(client, relayIp, relayPort));

		try {
			Future<String> future = service.take();
			if (future != null) {
				String host = future.get();
				if (!Util.isEmpty(host)) {
					return requestUrl.newBuilder().host(host).port(relayPort).build();
				}
			}
		} catch (InterruptedException | ExecutionException ignored) {
		}

		// shutdown executors
		executor.shutdownNow();

		return null;
	}

	private Callable<String> createPingTask(final OkHttpClient client, final String host, int port) {
		final HttpUrl pingPongUrl = new HttpUrl.Builder()
				.scheme(requestUrl.scheme())
				.host(host)
				.port(port)
				.addPathSegment("webman")
				.addPathSegment("pingpong.cgi")
				.addQueryParameter("action", "cors")
				.build();
		return new Callable<String>() {
			@Override
			public String call() throws Exception {
				Request request = new Request.Builder()
						.url(pingPongUrl)
						.build();
				Response response = client.newCall(request).execute();
				InputStream in = response.body().byteStream();
				JsonReader reader = null;
				try {
					reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
					PingPongJson pingPongJson = gson.fromJson(reader, PingPongJson.class);
					if (pingPongJson != null && pingPongJson.success) {
						return host;
					}
				} finally {
					if (reader != null) {
						reader.close();
					}
				}
				return null;
			}
		};
	}

	public ServerInfoJson requestTunnel(ServerInfoJson infoJson, String serverID, String id) throws IOException {
		if (infoJson == null
				|| infoJson.env == null
				|| Util.isEmpty(infoJson.env.control_host)) {
			return null;
		}

		// set timeout to 30 seconds
		OkHttpClient client = defaultClient.newBuilder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build();

		final String server = infoJson.env.control_host;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("command", "request_tunnel");
		jsonObject.addProperty("version", 1);
		jsonObject.addProperty("serverID", serverID);
		jsonObject.addProperty("id", id);

		RequestBody requestBody = RequestBody.create(
				MediaType.parse("text/plain"), gson.toJson(jsonObject));
		Request request = new Request.Builder()
				.url(HttpUrl.parse("http://" + server + "/Serv.php"))
				.post(requestBody)
				.build();
		Response response = client.newCall(request).execute();
		JsonReader reader = null;
		try {
			InputStream in = response.body().byteStream();
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			return gson.fromJson(reader, ServerInfoJson.class);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
