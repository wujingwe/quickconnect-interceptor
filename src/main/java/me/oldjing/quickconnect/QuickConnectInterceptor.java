package me.oldjing.quickconnect;

import com.squareup.okhttp.*;

import java.io.IOException;

import static me.oldjing.quickconnect.Connection.ID_DSM_PORTAL;
import static me.oldjing.quickconnect.Connection.ID_DSM_PORTAL_HTTPS;

public class QuickConnectInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		HttpUrl requestUrl = request.httpUrl();
		boolean isHttps = requestUrl.isHttps();
		String host = requestUrl.host();
		if (Util.isQuickConnectId(host)) {
			final String serverID = host;
			final String id = isHttps ? ID_DSM_PORTAL_HTTPS : ID_DSM_PORTAL;

			Connection connection = Util.getConnection(host);
			if (connection == null) {
				// no quick connect information yet!
				connection = Util.addConnection(serverID, id);
			}
			if (connection.resolvedUrl() == null) {
				// not resolved yet!
				QuickConnectResolver resolver = new QuickConnectResolver(requestUrl);
				connection = resolver.resolveUrl(serverID, id);
			}
			HttpUrl resolvedUrl = connection.resolvedUrl();
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
