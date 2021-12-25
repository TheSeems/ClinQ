package me.theseems.clinq.impl.token.special;

public class ErrorToken implements SpecialToken {
	private final String message;

	public ErrorToken(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ErrorToken{" +
			"message='" + message + '\'' +
			'}';
	}
}
