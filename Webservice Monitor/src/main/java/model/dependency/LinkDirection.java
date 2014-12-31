package model.dependency;

public enum LinkDirection {
	FORWARD(1), BOTH(0), BACKWARD(-1);

	private int direction;

	LinkDirection(int direction) {
		this.direction = direction;
	}

	public int getIntValue() {
		return direction;
	}
}
