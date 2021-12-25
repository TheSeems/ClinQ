package me.theseems.clinq.impl.token;

import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class WhenToken<T> implements Token {
	private final CheckToken<T> checkToken;
	private final CheckerToken<T, T> passCheckerToken;
	private final CheckerToken<T, T> failureCheckerToken;

	public WhenToken(CheckToken<T> checkToken,
					 CheckerToken<T, T> passCheckerToken,
					 CheckerToken<T, T> failureCheckerToken) {
		this.checkToken = checkToken;
		this.passCheckerToken = passCheckerToken;
		this.failureCheckerToken = failureCheckerToken;
	}

	public WhenToken(CheckToken<T> checkToken, CheckerToken<T, T> passCheckerToken) {
		this(checkToken, passCheckerToken, null);
	}

	public WhenToken(CheckToken<T> checkToken) {
		this(checkToken, null, null);
	}

	public CheckToken<T> getCheckToken() {
		return checkToken;
	}

	public CheckerToken<T, T> getPassCheckerToken() {
		return passCheckerToken;
	}

	public CheckerToken<T, T> getFailCheckerToken() {
		return failureCheckerToken;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
