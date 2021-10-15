package me.theseems.clinq.utils;

import me.theseems.clinq.api.Checker;
import me.theseems.clinq.api.ClinQ;
import me.theseems.clinq.api.check.Check;

public final class Checkers {
	public static <T> Checker<T, T> of(Check<T> check) {
		Checker<T, T> queueChecker = ClinQ.checker();
		queueChecker.with(check);
		return queueChecker;
	}
}
