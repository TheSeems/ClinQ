package ru.vbc.clinq.checks;

import ru.vbc.clinq.api.Check;

public class StringNotEmpty implements Check<String> {
	@Override
	public boolean check(String value) {
		return !value.isEmpty();
	}

	private StringNotEmpty() {}

	public static StringNotEmpty check() {
		return new StringNotEmpty();
	}
}
