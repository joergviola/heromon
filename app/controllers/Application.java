package controllers;

import play.Logger;
import play.mvc.Controller;

public class Application extends Controller {

	public static void register(String app, String uid, String pwd) {
		Logger.info("Register %s %s %s", app, uid, pwd);
		render();
	}

}