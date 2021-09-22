package me.theseems.clinq;

import me.theseems.clinq.dto.SampleDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.theseems.clinq.api.ClinQ;
import ru.theseems.clinq.checks.IntegerIs;
import ru.theseems.clinq.checks.StringMatches;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ClinqCheckerTest {
	@Test
	public void validateInteger_WithLogicalCheckCombination_Mapping_Pipe_Success() {
		Map<Double, Boolean> coolMap = Map.of(
			0.1, false,
			6.0, true
		);

		var checker =
			ClinQ.checker(Integer.class)
				.and(i -> i % 2 == 0, i -> i % 3 == 0)
				.map(Double::valueOf)
				.map(d -> d / 2.0)
				.pipe(value -> Optional.ofNullable(coolMap.get(value)))
				.with(i -> i);

		Assertions.assertTrue(checker.check(12));
		Assertions.assertFalse(checker.check(3));
	}

	@Test
	public void validateInteger_WithLogicalCheckCombination_Mapping_Pipe_Stress_Success() {
		var checker =
			ClinQ.checker(Integer.class)
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
	public void validateInteger_WithExternalCheck_Success() {
		var checker =
			ClinQ.checker(Integer.class)
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
	public void validateString_WithExternalCheck_Mapping_Success() {
		var checker = ClinQ.checker(String.class)
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
	public void validateDto_WithNestedCheckers_Success() {
		var checker =
			ClinQ.checker(SampleDto.class)
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

	@Test
	public void validateDto_WithConditionalChecks_Success() {
		var shortNameChecker =
			ClinQ.checker(String.class)
				.with(name -> name.startsWith("s"))
				.mapCheck(name -> name.charAt(1), Character::isDigit);

		var mediumNameChecker =
			ClinQ.checker(String.class)
				.with(name -> name.startsWith("m"))
				.mapCheck(name -> name.charAt(1), Character::isDigit)
				.mapCheck(name -> name.charAt(2), Character::isLetter);

		var dtoChecker = ClinQ.checker(SampleDto.class);

		// Name checking attached
		dtoChecker.with(SampleDto::getName, it ->
			it.and(Objects::nonNull, name -> 1 <= name.length() && name.length() <= 3)
				.when(name -> name.length() <= 2, shortNameChecker)
				.when(name -> name.length() == 3, mediumNameChecker)
		);

		SampleDto shortNameDto = new SampleDto("s0", List.of());
		SampleDto mediumNameDto = new SampleDto("m0a", List.of());
		SampleDto invalidDto = new SampleDto("bigger-than-3", List.of());

		Assertions.assertTrue(dtoChecker.check(shortNameDto));
		Assertions.assertTrue(dtoChecker.check(mediumNameDto));
		Assertions.assertFalse(dtoChecker.check(invalidDto));
	}
}
