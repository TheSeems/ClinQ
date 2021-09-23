package ru.theseems.clinq.checks;

import ru.theseems.clinq.api.Check;

import java.util.regex.Pattern;

public class StringMatches implements Check<String> {
	private final Pattern pattern;

	private StringMatches(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean check(String value) {
		return pattern.matcher(value).matches();
	}

	public static StringMatches pattern(Pattern pattern) {
		return new StringMatches(pattern);
	}

	public static StringMatches pattern(String regex) {
		return new StringMatches(Pattern.compile(regex));
	}
}
