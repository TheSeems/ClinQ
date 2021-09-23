package me.theseems.clinq;

import me.theseems.clinq.dto.SampleDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vbc.clinq.api.Checker;
import ru.vbc.clinq.api.Clinq;
import ru.vbc.clinq.checks.IntegerIs;
import ru.vbc.clinq.checks.StringMatches;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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
	public void test1_Stress() {
		var checker =
			Clinq.checker(Integer.class)
				.and(i -> i % 2 == 0, i -> i % 3 == 0);

		for (int i = 0; i < 10_000_000; i++) {
			if (i % 6 == 0) {
				Assertions.assertTrue(checker.check(i));
			} else {
				Assertions.assertFalse(checker.check(i));
			}
		}
	}

	@Test
	public void test2() {
		var checker = Clinq.checker(String.class)
			.with(str -> !str.isEmpty())
			.with(StringMatches.pattern("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"))
			.map(value -> value.split("@")[0])
			.with(value -> value.length() == 2);

		Assertions.assertFalse(checker.check("sl;dfglksdb"));
		Assertions.assertFalse(checker.check("a@test.ru"));
		Assertions.assertTrue(checker.check("ab@test.ru"));
		Assertions.assertFalse(checker.check("abc@test.ru"));
	}

	@Test
	public void test3() {
		var checker =
			Clinq.checker(Integer.class)
				// a prime lesser than 100
				.and(IntegerIs.prime(), i -> i < 100)
				.with(i -> i % 2 != 0);

		Assertions.assertTrue(checker.check(3));
		Assertions.assertFalse(checker.check(4));

		Assertions.assertTrue(checker.check(5));
		Assertions.assertTrue(checker.check(7));
		Assertions.assertFalse(checker.check(9));

		Assertions.assertFalse(checker.check(1_000_000_000 + 7));
	}

	@Test
	public void test4() {
		var checker =
			Clinq.checker(SampleDto.class)
				.with(Objects::nonNull)
				.with(SampleDto::getName, nameChecker ->
					nameChecker
						.with(Objects::nonNull)
						.with(str -> str.length() > 2))
				.with(SampleDto::getScores, scoresChecker ->
					scoresChecker
						.with(Objects::nonNull)
						.with(scores -> scores.size() > 2)
						.with(scores -> scores.stream().allMatch(score -> score > 0)))
				.with(dto -> dto.getName().length() == dto.getScores().size());

		SampleDto one = new SampleDto("hel", List.of(1, 2, 3));
		Assertions.assertTrue(checker.check(one));
	}
}
