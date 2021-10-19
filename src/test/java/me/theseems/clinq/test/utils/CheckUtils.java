package me.theseems.clinq.test.utils;

import java.time.LocalDate;

public class CheckUtils {
	/**
	 * Check date {@code from} is before date {@code to}
	 *
	 * @param from date - left border
	 * @param to   date - right border
	 * @return {@literal true} if left border is before right border, otherwise {@literal false}
	 */
	public static boolean isBefore(LocalDate from, LocalDate to) {
		return from == null || to == null || from.isBefore(to);
	}
}
