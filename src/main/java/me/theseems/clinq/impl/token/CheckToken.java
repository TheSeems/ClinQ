package me.theseems.clinq.impl.token;

import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class CheckToken<T> implements Token {
	private final Check<T> check;

	public CheckToken(Check<T> check) {
		this.check = check;
	}

	public Check<T> getCheck() {
		return check;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
