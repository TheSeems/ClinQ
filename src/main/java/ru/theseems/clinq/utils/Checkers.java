package ru.theseems.clinq.utils;

import ru.theseems.clinq.api.Check;
import ru.theseems.clinq.api.Checker;
import ru.theseems.clinq.api.ClinQ;

public final class Checkers {
	public static <T> Checker<T, T> of(Check<T> check) {
		Checker<T, T> queueChecker = ClinQ.checker();
		queueChecker.with(check);
		return queueChecker;
	}
}
