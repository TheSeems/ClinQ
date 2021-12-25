package me.theseems.clinq.api;

import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.impl.checker.QueueChecker;

public final class Clinq {
	public static <InputType> Checker<InputType, InputType> checker() {
		return new QueueChecker<>();
	}
}
