package controllers;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import play.Logger;

public class Ping {

	public static void ping() {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://heromon.herokuapp.com");
		try {
			client.execute(get);
		} catch (Exception e) {
			Logger.error(e, "During ping");
		}
	}

}
