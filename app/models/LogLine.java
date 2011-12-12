package models;

public class LogLine {

	public String uri;
	public String dyno;
	public int queue;
	public int wait;
	public int service;
	public int status;
	public int length;

	public LogLine(String line) {
		String[] col = line.split(" ");
		uri = col[3];
		dyno = value(col[4]);
		queue = Integer.parseInt(value(col[5]));
		wait = Integer.parseInt(valueMS(col[6]));
		service = Integer.parseInt(valueMS(col[7]));
		status = Integer.parseInt(valueMS(col[8]));
		length = Integer.parseInt(value(col[9]));
	}

	private String valueMS(String assign) {
		String value = value(assign);
		return value.substring(0, value.length() - 2);
	}

	private String value(String assign) {
		String[] parts = assign.split("=");
		return parts[1];
	}

}
