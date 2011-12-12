package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import models.App;
import models.Result;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;

import play.Logger;
import play.mvc.Controller;

public class Application extends Controller {

	public static void register(String app, String apikey, int frequency)
			throws ClientProtocolException, IOException {
		Logger.info("Register %s XXX %d", app, frequency);
		App application = App.findById(app);
		if (application != null)
			error("Already registered.");
		application = new App(app, apikey, frequency);
		application.save();
		InputStream logs = application.snapshot();
		renderBinary(logs);
	}

	public static void snapshot(String app) throws ClientProtocolException,
			IOException {
		Logger.info("Snapshot %s", app);
		App application = App.findById(app);
		if (application == null)
			error("Not registered.");
		InputStream logs = application.snapshot();
		renderBinary(logs);
	}

	public static void analyse(String app) throws ClientProtocolException,
			IOException {
		Logger.info("Analyse %s", app);
		App application = App.findById(app);
		if (application == null)
			error("Not registered.");
		Collection<Result> results = application.analyse();
		StringBuilder b = new StringBuilder();
		for (Result result : results) {
			b.append(result.toString("max"));
			b.append("\n");
		}
		renderText(b.toString());
	}

	public static void query(String app, Integer days, String url, String mode)
			throws ClientProtocolException, IOException {
		Logger.info("Query %s %d %s %s", app, days, url, mode);
		if (StringUtils.isEmpty(url))
			url = null;
		if (StringUtils.isEmpty(mode))
			mode = "max";
		App application = App.findById(app);
		if (application == null)
			error("Not registered.");
		Collection<Result> results = application.query(days, url);
		StringBuilder b = new StringBuilder();
		for (Result result : results) {
			b.append(result.toString(mode));
			b.append("\n");
		}
		renderText(b.toString());
	}
}