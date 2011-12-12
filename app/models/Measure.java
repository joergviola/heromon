package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.db.jpa.Model;

@Entity
public class Measure extends Model {

	@ManyToOne
	public App app;

	public Date date;

	@OneToMany(mappedBy = "measure")
	@OrderBy("uri")
	public List<Result> results;

	public Measure(App app, Date date) {
		this.app = app;
		this.date = date;
	}
}
