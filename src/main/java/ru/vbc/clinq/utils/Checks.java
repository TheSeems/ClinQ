package ru.vbc.clinq.utils;

import ru.vbc.clinq.api.Check;

import java.util.Arrays;

public class Checks {
	public static <T> Check<T> truth() {
		return value -> true;
	}

	public static <T> Check<T> lie() {
		return value -> false;
	}

	@SafeVarargs
	public static <T> Check<T> and(Check<T>... checks) {
		return value -> Arrays.stream(checks).allMatch(check -> check.check(value));
	}

	@SafeVarargs
	public static <T> Check<T> and(Class<T> clazz, Check<T>... checks) {
		return and(checks);
	}

	@SafeVarargs
	public static <T> Check<T> or(Check<T>... checks) {
		return value -> Arrays.stream(checks).anyMatch(check -> check.check(value));
	}

	@SafeVarargs
	public static <T> Check<T> or(Class<T> clazz, Check<T>... checks) {
		return or(checks);
	}
}
