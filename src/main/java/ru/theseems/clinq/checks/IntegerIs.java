package ru.theseems.clinq.checks;

import ru.theseems.clinq.api.Check;

import java.util.stream.IntStream;

public class IntegerIs {
	private static class Prime implements Check<Integer> {
		@Override
		public boolean check(Integer value) {
			if (value < 2) {
				return false;
			}

			return IntStream
				.iterate(2, i -> i * i <= value, i -> i + 1)
				.noneMatch(i -> value % i == 0);
		}
	}

	public static Prime prime() {
		return new Prime();
	}
}
