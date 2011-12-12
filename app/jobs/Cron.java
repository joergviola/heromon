package jobs;

import java.util.Collection;

import models.App;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
@Every("15min")
public class Cron extends Job {
	@Override
	public void doJob() throws Exception {
		Collection<App> apps = App.findWaiting();
		for (App app : apps) {
			app.analyse();
		}
	}
}
