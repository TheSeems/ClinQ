package me.theseems.clinq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vbc.clinq.api.Clinq;
import ru.vbc.clinq.checks.StringMatches;
import ru.vbc.clinq.checks.StringNotEmpty;

import java.util.Map;
import java.util.Optional;

public class ClinqCheckerTest {
	@Test
	public void test1() {
		Map<Double, Boolean> coolMap = Map.of(
			0.1, false,
			6.0, true
		);

		var checker =
			Clinq.checker(Integer.class)
				.and(i -> i % 2 == 0, i -> i % 3 == 0)
				.map(Double::valueOf)
				.map(d -> d / 2.0)
				.pipe(value -> Optional.ofNullable(coolMap.get(value)))
				.with(i -> i);

		Assertions.assertTrue(checker.check(12));
		Assertions.assertFalse(checker.check(3));
	}

	@Test
	public void test2() {
		var checker = Clinq.checker(String.class)
			.with(StringNotEmpty.check())
			.with(StringMatches.pattern("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"))
			.map(value -> value.split("@")[0])
			.with(value -> value.length() == 2);

		Assertions.assertFalse(checker.check("sl;dfglksdb"));
		Assertions.assertFalse(checker.check("a@test.ru"));
		Assertions.assertTrue(checker.check("ab@test.ru"));
		Assertions.assertFalse(checker.check("abc@test.ru"));
	}
}
