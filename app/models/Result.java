package models;

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

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(uri);
		b.append(" ");
		b.append(maxQueue);
		b.append(" ");
		b.append(maxWait);
		b.append(" ");
		b.append(maxService);
		b.append(" ");
		b.append(maxLength);
		return b.toString();
	}

}
