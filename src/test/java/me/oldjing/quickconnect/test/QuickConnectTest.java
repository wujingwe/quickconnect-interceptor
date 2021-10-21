package me.oldjing.quickconnect.test;

import com.google.gson.Gson;
import me.oldjing.quickconnect.QuickConnectInterceptor;
import me.oldjing.quickconnect.QuickConnectResolver;
import me.oldjing.quickconnect.json.PingPongJson;
import me.oldjing.quickconnect.json.ServerInfoJson;
import me.oldjing.quickconnect.json.ServerInfoJson.EnvJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson.ExternalJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServerJson.InterfaceJson;
import me.oldjing.quickconnect.json.ServerInfoJson.ServiceJson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuickConnectTest {

	@Rule
	public MockWebServer server = new MockWebServer();

	private Gson gson = new Gson();

	@Test
	public void serverInfo() throws Exception {
		final List<InterfaceJson> interfaceJsonList = new ArrayList<>();
		interfaceJsonList.add(new ServerInfoJson.ServerJson.InterfaceJson("localhost", null, "mask", "eth0"));
		final ExternalJson externalJson = new ExternalJson("external", "external_ipv6");
		final ServerJson serverJson = new ServerJson("dsm", "ddns", "fqdn", "gateway", interfaceJsonList, externalJson);
		final EnvJson envJson = new EnvJson("tw", "twc.quickconnect.to");
		final ServiceJson serviceJson = new ServiceJson(5000, 0, null, null, 0);
		final ServerInfoJson serverInfoJson = new ServerInfoJson(null, serverJson, envJson, serviceJson);
		server.enqueue(new MockResponse().setBody(gson.toJson(serverInfoJson)));

		QuickConnectResolver resolver = new QuickConnectResolver(server.url("/"));
		ServerInfoJson result = resolver.getServerInfo(server.url("/"), "demo", "dsm_portal");

		assertNotNull(result);
		assertEquals(result, serverInfoJson);
	}

	@Test
	public void pingDSM() throws Exception {
		PingPongJson pingPongJson = new PingPongJson(true);
		server.enqueue(new MockResponse().setBody(gson.toJson(pingPongJson)));

		final List<InterfaceJson> interfaceJsonList = new ArrayList<>();
		interfaceJsonList.add(new InterfaceJson("localhost", null, "mask", "eth0"));
		final ServerJson serverJson = new ServerJson("dsm", "NULL", "NULL", "gateway", interfaceJsonList, null);
		final EnvJson envJson = new EnvJson("tw", "twc.quickconnect.to");
		final ServiceJson serviceJson = new ServiceJson(server.getPort(), 0, null, null, 0);
		final ServerInfoJson serverInfoJson = new ServerInfoJson(null, serverJson, envJson, serviceJson);
		final QuickConnectResolver resolver = new QuickConnectResolver(server.url("/"));
		final HttpUrl url = resolver.pingDSM(serverInfoJson);

		assertNotNull(url);
		assertEquals(url, server.url("/"));
	}

	@Test
	public void requestTunnel() throws Exception {
		final List<InterfaceJson> interfaceJsonList = new ArrayList<>();
		interfaceJsonList.add(new InterfaceJson("localhost", null, "mask", "eth0"));
		final ExternalJson externalJson = new ExternalJson("external", "external_ipv6");
		final ServerJson serverJson = new ServerJson("dsm", "ddns", "fqdn", "gateway", interfaceJsonList, externalJson);
		final EnvJson envJson = new EnvJson("tw", server.url("/").host() + ":" + server.getPort());
		final ServiceJson serviceJson = new ServiceJson(5000, 0, null, null, 0);
		final ServerInfoJson serverInfoJson = new ServerInfoJson(null, serverJson, envJson, serviceJson);
		server.enqueue(new MockResponse().setBody(gson.toJson(serverInfoJson)));

		QuickConnectResolver resolver = new QuickConnectResolver(server.url("/"));
		ServerInfoJson result = resolver.requestTunnel(serverInfoJson, "demo", "dsm_portal");

		assertNotNull(result);
		assertEquals(result, serverInfoJson);
	}

	@Ignore
	@Test
	public void serverID_demo() throws Exception {
        // add Quick Connect interceptor
		OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new QuickConnectInterceptor())
                .build();

		Request request = new Request.Builder()
				.url(HttpUrl.parse("http://demo/webman/pingpong.cgi?action=cors"))
				.build();

		Response response = client.newCall(request).execute();
		String responseString = response.body().string();
		System.out.println(responseString);
		assertNotNull(responseString);
	}

	@Ignore
	@Test
	public void serverID_dsm() throws Exception {
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

		// add Quick Connect interceptor
        builder.addInterceptor(new QuickConnectInterceptor());
        OkHttpClient client = builder.build();

		Request request = new Request.Builder()
				.url(HttpUrl.parse("https://dsm/webman/pingpong.cgi?action=cors"))
				.build();
		Response response = client.newCall(request).execute();
		String responseString = response.body().string();
		System.out.println(responseString);
		assertNotNull(responseString);
	}
}
