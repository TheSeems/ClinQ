package me.theseems.clinq.checks;

import me.theseems.clinq.api.check.Check;

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
