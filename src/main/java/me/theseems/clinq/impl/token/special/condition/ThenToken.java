package me.theseems.clinq.impl.token.special.condition;

import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.impl.token.CheckerToken;

public class ThenToken<T, V>implements ConditionToken {
	private final Checker<T, V> checker;

	public ThenToken(Checker<T, V> checker) {
		this.checker = checker;
	}

	public Checker<T, V> getChecker() {
		return checker;
	}

	public static <T> ThenToken<T, T> of(CheckerToken<T, T> checkerToken) {
		if (checkerToken == null) {
			return null;
		}

		return new ThenToken<>(checkerToken.getChecker());
	}
}
