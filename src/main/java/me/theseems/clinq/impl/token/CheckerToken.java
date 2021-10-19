package me.theseems.clinq.impl.token;

import me.theseems.clinq.api.Checker;
import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class CheckerToken<T, V> implements Token {
	private final Checker<T, V> checker;

	public CheckerToken(Checker<T, V> checker) {
		this.checker = checker;
	}

	public Checker<T, V> getChecker() {
		return checker;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
