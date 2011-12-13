package jobs;

import java.util.Collection;

import models.App;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import controllers.Ping;

@OnApplicationStart
@Every("15min")
public class Cron extends Job {
	@Override
	public void doJob() throws Exception {
		long start = System.currentTimeMillis();
		Collection<App> apps = App.findWaiting();
		for (App app : apps) {
			app.analyse();
		}
		long ms = System.currentTimeMillis() - start;
		Logger.info("Monitored %d app in %d ms", apps.size(), ms);
		Ping.ping();
	}
}
