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

	public static void index() {
		long apps = App.count();
		render(apps);
	}

	public static void register(String app, String apikey, int frequency)
			throws ClientProtocolException, IOException {
		Logger.info("Register %s XXX %d", app, frequency);
		App application = App.findById(app);
		if (application != null)
			renderText("Already registered");
		application = new App(app, apikey, frequency);
		application.save();
		Collection<Result> results = application.analyse();
		ResultBuilder b = new ResultBuilder("max");
		renderText(b.toString(results));
	}

	public static void update(String app, Integer frequency)
			throws ClientProtocolException, IOException {
		Logger.info("Update %s", app);
		App application = App.findById(app);
		if (application == null)
			renderText("App not found");
		if (frequency != null)
			application.frequency = frequency;
		application.save();
		renderText("Done.");
	}

	public static void remove(String app, String apikey)
			throws ClientProtocolException, IOException {
		Logger.info("Remove %s", app);
		App application = App.findById(app);
		if (application == null)
			renderText("App not found");
		if (!application.apikey.equals(apikey))
			renderText("Invalid key");
		application.delete();
		renderText("Done.");
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
		ResultBuilder b = new ResultBuilder("max");
		renderText(b.toString(results));
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
		ResultBuilder b = new ResultBuilder(mode);
		renderText(b.toString(results));
	}
}