package me.theseems.clinq.utils;

import me.theseems.clinq.api.Checker;
import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.check.Check;

public final class Checkers {
	public static <T> Checker<T, T> of(Check<T> check) {
		Checker<T, T> queueChecker = Clinq.checker();
		queueChecker.with(check);
		return queueChecker;
	}
}
