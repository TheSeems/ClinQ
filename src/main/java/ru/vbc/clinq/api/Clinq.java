package ru.vbc.clinq.api;

import ru.vbc.clinq.impl.checker.SimpleChecker;

public final class Clinq {
	public static <InputType> Checker<InputType, InputType> checker() {
		return new SimpleChecker<>();
	}

	public static <InputType> Checker<InputType, InputType> checker(Class<InputType> clazz) {
		return new SimpleChecker<>();
	}
}
