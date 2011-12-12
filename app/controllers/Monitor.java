package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import models.App;
import models.LogLine;
import models.Measure;
import models.Result;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Monitor {

	public static Collection<Result> analyse(App app)
			throws ClientProtocolException, IOException {
		InputStream in = snapshot(app);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Measure measure = new Measure(app, new Date());
		Result result = new Result(measure, "all");
		HashMap<String, Result> results = new HashMap<String, Result>();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			try {
				LogLine logLine = new LogLine(line);
				result.add(logLine);
				Result r = results.get(logLine.uri);
				if (r == null) {
					r = new Result(measure, logLine.uri);
					results.put(logLine.uri, r);
				}
				r.add(logLine);
			} catch (Throwable t) {
				continue;
			}
		}
		results.put(result.uri, result);
		Collection<Result> list = results.values();
		measure.save();
		for (Result r : list) {
			r.save();
		}
		return list;
	}

	public static InputStream snapshot(App app) throws ClientProtocolException,
			IOException {
		DefaultHttpClient client = createClient();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("",
				app.apikey);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, creds);
		client.setCredentialsProvider(credsProvider);
		HttpGet logurl = new HttpGet("https://api.heroku.com/apps/" + app.name
				+ "/logs?logplex=true&ps=router&num=1500");
		logurl.addHeader("Accept", "application/xml");
		HttpResponse response = client.execute(logurl);
		String logsessionURL = EntityUtils.toString(response.getEntity());
		HttpGet logsession = new HttpGet(logsessionURL);
		logsession.addHeader("Accept", "application/xml");
		response = client.execute(logsession);
		return response.getEntity().getContent();
	}

	public static DefaultHttpClient createClient() {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return client;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
