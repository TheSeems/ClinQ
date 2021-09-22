package ru.vbc.clinq.impl.token;

import ru.vbc.clinq.api.Check;

public class CheckToken<T> implements Token {
	private final Check<T> check;

	public CheckToken(Check<T> check) {
		this.check = check;
	}

	public Check<T> getCheck() {
		return check;
	}

	@Override
	public boolean accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
