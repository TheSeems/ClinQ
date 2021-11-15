package me.theseems.clinq.utils;

import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.impl.check.ConfiguredCheck;

public final class Checkers {
	public static <T> Checker<T, T> of(Check<T> check) {
		Checker<T, T> queueChecker = Clinq.checker();
		queueChecker.with(check);
		return queueChecker;
	}

	public static <T> Checker<T, T> of(String message, Check<T> check) {
		return of(ConfiguredCheck.message(check, message));
	}
}
