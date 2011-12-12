package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import models.App;
import models.Result;

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
		InputStream logs = Monitor.snapshot(application);
		renderBinary(logs);
	}

	public static void snapshot(String app) throws ClientProtocolException,
			IOException {
		Logger.info("Snapshot %s", app);
		App application = App.findById(app);
		if (application == null)
			error("Not registered.");
		InputStream logs = Monitor.snapshot(application);
		renderBinary(logs);
	}

	public static void analyse(String app) throws ClientProtocolException,
			IOException {
		Logger.info("Analyse %s", app);
		App application = App.findById(app);
		if (application == null)
			error("Not registered.");
		Collection<Result> results = Monitor.analyse(application);
		StringBuilder b = new StringBuilder();
		for (Result result : results) {
			b.append(result.toString());
			b.append("\n");
		}
		renderText(b.toString());
	}
}