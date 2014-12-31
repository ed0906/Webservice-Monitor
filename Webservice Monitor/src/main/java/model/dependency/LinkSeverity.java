package model.dependency;

public enum LinkSeverity {
	STRONG(2), WEAK(1);

	private int severity;

	LinkSeverity(int severity) {
		this.severity = severity;
	}

	public int getIntValue() {
		return severity;
	}

}
