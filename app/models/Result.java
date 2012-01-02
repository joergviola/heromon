package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.jpa.GenericModel;

@Entity
public class Result extends GenericModel {

	@Id
	@ManyToOne
	public Measure measure;
	@Id
	public String uri;
	@ManyToOne
	public App app;
	public Date date;
	public int count;
	public int queue;
	public int wait;
	public int service;
	public int status;
	public int length;
	public int maxQueue;
	public int maxWait;
	public int maxService;
	public int maxLength;

	public Result(Measure measure, String uri) {
		this.measure = measure;
		this.uri = uri;
		this.app = measure.app;
		this.date = measure.date;
	}

	public void add(LogLine logLine) {
		count++;
		status = logLine.status;
		queue += logLine.queue;
		wait += logLine.wait;
		service += logLine.service;
		length += logLine.length;
		maxQueue = Math.max(maxQueue, logLine.queue);
		maxWait = Math.max(maxWait, logLine.wait);
		maxService = Math.max(maxService, logLine.service);
		maxLength = Math.max(maxLength, logLine.length);
	}

	public String toString(String mode) {
		StringBuilder b = new StringBuilder();
		b.append(measure.date);
		b.append(" ");
		b.append(uri);
		b.append(" ");
		b.append(count);
		b.append(" ");
		if ("max".equals(mode)) {
			b.append(maxQueue);
			b.append(" ");
			b.append(maxWait);
			b.append(" ");
			b.append(maxService);
			b.append(" ");
			b.append(maxLength);
		} else {
			if (count > 0) {
				b.append(queue / count);
				b.append(" ");
				b.append(wait / count);
				b.append(" ");
				b.append(service / count);
				b.append(" ");
				b.append(length / count);
			}
		}
		return b.toString();
	}

}
