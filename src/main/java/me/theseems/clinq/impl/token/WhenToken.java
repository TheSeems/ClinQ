package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class WhenToken<T> implements Token {
	private final CheckToken<T> checkToken;
	private final CheckerToken<T, T> checkerToken;

	public WhenToken(CheckToken<T> checkToken, CheckerToken<T, T> checkerToken) {
		this.checkToken = checkToken;
		this.checkerToken = checkerToken;
	}

	public CheckToken<T> getCheckToken() {
		return checkToken;
	}

	public CheckerToken<T, T> getCheckerToken() {
		return checkerToken;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
