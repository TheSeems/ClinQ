package ru.theseems.clinq.api;

import ru.theseems.clinq.impl.checker.QueueChecker;

public final class ClinQ {
	public static <InputType> Checker<InputType, InputType> checker() {
		return new QueueChecker<>();
	}

	public static <InputType> Checker<InputType, InputType> checker(Class<InputType> clazz) {
		return new QueueChecker<>();
	}
}
