package controllers;

import java.util.Collection;
import java.util.Date;

import models.Result;

import org.apache.commons.lang.StringUtils;

public class ResultBuilder {

	public interface Visitor {
		void accept(Date date, String uri, int count, int queue, int wait,
				int service, int length);
	}

	private final String mode;
	private int[] lengths;
	private StringBuilder builder;

	public ResultBuilder(String mode) {
		this.mode = mode;
	}

	public String toString(Collection<Result> results) {
		lengths = new int[7];
		calcMaxLength(results);
		builder = new StringBuilder();
		print(results);
		return builder.toString();
	}

	private void calcMaxLength(Collection<Result> results) {
		maxim(0, "Date");
		maxim(1, "URL");
		maxim(2, "Count");
		maxim(3, "Queue");
		maxim(4, "Wait");
		maxim(5, "Service");
		maxim(6, "Length");
		iterate(results, new Visitor() {
			public void accept(Date date, String uri, int count, int queue,
					int wait, int service, int length) {
				maxim(0, date);
				maxim(1, uri);
				maxim(2, count);
				maxim(3, queue);
				maxim(4, wait);
				maxim(5, service);
				maxim(6, length);
			}
		});
	}

	private void maxim(int index, Object o) {
		if (o != null)
			lengths[index] = Math.max(lengths[index], o.toString().length());
	}

	private void print(Collection<Result> results) {
		builder.append("| ");
		builder.append(StringUtils.rightPad("Date", lengths[0]));
		builder.append(" | ");
		builder.append(StringUtils.rightPad("URL", lengths[1]));
		builder.append(" | ");
		builder.append(StringUtils.leftPad("Count", lengths[2]));
		builder.append(" | ");
		builder.append(StringUtils.leftPad("Queue", lengths[3]));
		builder.append(" | ");
		builder.append(StringUtils.leftPad("Wait", lengths[4]));
		builder.append(" | ");
		builder.append(StringUtils.leftPad("Service", lengths[5]));
		builder.append(" | ");
		builder.append(StringUtils.leftPad("Length", lengths[6]));
		builder.append(" |");
		builder.append("\n");
		iterate(results, new Visitor() {
			public void accept(Date date, String uri, int count, int queue,
					int wait, int service, int length) {
				builder.append("| ");
				builder.append(StringUtils.rightPad(date.toString(), lengths[0]));
				builder.append(" | ");
				builder.append(StringUtils.rightPad(uri, lengths[1]));
				builder.append(" | ");
				builder.append(StringUtils.leftPad(Integer.toString(count),
						lengths[2]));
				builder.append(" | ");
				builder.append(StringUtils.leftPad(Integer.toString(queue),
						lengths[3]));
				builder.append(" | ");
				builder.append(StringUtils.leftPad(Integer.toString(wait),
						lengths[4]));
				builder.append(" | ");
				builder.append(StringUtils.leftPad(Integer.toString(service),
						lengths[5]));
				builder.append(" | ");
				builder.append(StringUtils.leftPad(Integer.toString(length),
						lengths[6]));
				builder.append(" |");
				builder.append("\n");
			}
		});
	}

	private void iterate(Collection<Result> results, Visitor visitor) {
		for (Result result : results) {
			String uri = result.uri;
			if (uri.length() > 60)
				uri = uri.substring(0, 57) + "...";
			if ("max".equals(mode)) {
				visitor.accept(result.date, uri, result.count, result.maxQueue,
						result.maxWait, result.maxService, result.maxLength);
			} else {
				if (result.count > 0) {
					visitor.accept(result.date, uri, result.count, result.queue
							/ result.count, result.wait / result.count,
							result.service / result.count, result.length
									/ result.count);
				} else {
					visitor.accept(result.date, uri, result.count, 0, 0, 0, 0);
				}
			}
		}
	}
}
