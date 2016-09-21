package me.oldjing.quickconnect;

import me.oldjing.quickconnect.store.RelayCookie;
import me.oldjing.quickconnect.store.RelayHandler;
import me.oldjing.quickconnect.store.RelayManager;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class QuickConnectInterceptor implements Interceptor {

	public static final String ID_DSM_PORTAL = "dsm_portal";
	public static final String ID_DSM_PORTAL_HTTPS = "dsm_portal_https";

	private RelayManager relayManager;

	public QuickConnectInterceptor() {
		relayManager = new RelayManager();
		RelayHandler.setDefault(relayManager);
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		HttpUrl requestUrl = request.url();
		boolean isHttps = requestUrl.isHttps();
		String host = requestUrl.host();
		if (Util.isQuickConnectId(host)) {
			final String serverID = host;
			final String id = isHttps ? ID_DSM_PORTAL_HTTPS : ID_DSM_PORTAL;

			RelayCookie cookie = relayManager.get(serverID);
			if (cookie == null) {
				// no quick connect information yet!
				cookie = new RelayCookie.Builder()
						.serverID(serverID)
						.id(id)
						.build();
			}
			if (cookie.resolvedUrl() == null) {
				// not resolved yet!
				QuickConnectResolver resolver = new QuickConnectResolver(requestUrl);
				cookie = resolver.resolve(serverID, id);

				// update cache
				relayManager.put(serverID, cookie);
			}
			HttpUrl resolvedUrl = cookie.resolvedUrl();
			if (resolvedUrl == null) {
				throw new IOException("resolvedUrl == null");
			}
			host = resolvedUrl.host();
			if (host.indexOf(':') != -1) {
				host = "[" + host + "]"; // add brackets for IPv6
			}
			HttpUrl url = requestUrl.newBuilder()
					.host(host)
					.port(resolvedUrl.port())
					.build();
			request = request.newBuilder()
					.url(url)
					.build();
			System.out.println("Resolved url: " + url);
		}
		return chain.proceed(request);
	}
}
