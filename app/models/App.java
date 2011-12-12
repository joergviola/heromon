package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.jpa.GenericModel;

@Entity
public class App extends GenericModel {
	@Id
	public String name;

	public String apikey;

	public int frequency;

	public App(String name, String apikey, int frequency) {
		this.name = name;
		this.apikey = apikey;
		this.frequency = frequency;
	}
}
