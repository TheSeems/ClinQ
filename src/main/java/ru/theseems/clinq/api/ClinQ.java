package ru.theseems.clinq.api;

import ru.theseems.clinq.impl.checker.SimpleChecker;

public final class ClinQ {
	public static <InputType> Checker<InputType, InputType> checker() {
		return new SimpleChecker<>();
	}

	public static <InputType> Checker<InputType, InputType> checker(Class<InputType> clazz) {
		return new SimpleChecker<>();
	}
}
