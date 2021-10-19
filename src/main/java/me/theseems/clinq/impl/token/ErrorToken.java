package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class ErrorToken implements Token {
	private final String message;

	public ErrorToken(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		throw new IllegalStateException("Visitor should not visit special tokens");
	}

	@Override
	public String toString() {
		return "ErrorToken{" +
			"message='" + message + '\'' +
			'}';
	}
}
