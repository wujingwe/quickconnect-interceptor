import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import me.oldjing.quickconnect.QuickConnectInterceptor;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.*;

public class QuickConnectTest {
	@Test
	public void test_demo() {
		OkHttpClient client = new OkHttpClient();

		// add Quick Connect interceptor
		client.interceptors()
				.add(new QuickConnectInterceptor());

		Request request = new Request.Builder()
				.url(HttpUrl.parse("http://demo/webman/pingpong.cgi?action=cors"))
				.build();
		try {
			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			System.out.println(responseString);
			assertNotNull(responseString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertNull(ioe);
		}
	}

	@Test
	public void test_dsm() {
		OkHttpClient client = new OkHttpClient();
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
			client.setSslSocketFactory(context.getSocketFactory());
			client.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					// since most DSM doesn't have valid certificate, ignore verifying hostname
					return true;
				}
			});
		} catch (NoSuchAlgorithmException | KeyManagementException ignored) {}

		// add Quick Connect interceptor
		client.interceptors()
				.add(new QuickConnectInterceptor());

		Request request = new Request.Builder()
				.url(HttpUrl.parse("https://dsm/webman/pingpong.cgi?action=cors"))
				.build();
		try {
			Response response = client.newCall(request).execute();
			String responseString = response.body().string();
			System.out.println(responseString);
			assertNotNull(responseString);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertNull(ioe);
		}
	}
}
