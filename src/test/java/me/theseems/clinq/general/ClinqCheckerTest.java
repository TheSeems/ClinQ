package me.theseems.clinq.general;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.checks.IntegerIs;
import me.theseems.clinq.checks.StringMatches;
import me.theseems.clinq.test.TestCheckErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
			Clinq.<Integer>checker()
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
			Clinq.<Integer>checker()
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
		TestCheckErrors errors = new TestCheckErrors();
		var checker =
			Clinq.<Integer>checker()
				// a prime lesser than 100
				.and("Input should be prime less than 100", IntegerIs.prime(), i -> i < 100)
				.with("Input should not be divisible by 2", i -> i % 2 != 0);

		Assertions.assertTrue(checker.check(3, errors));

		Assertions.assertFalse(checker.check(4, errors));
		errors.assertSame(List.of(
			"Input should be prime less than 100",
			"Input should not be divisible by 2"));

		Assertions.assertTrue(checker.check(5, errors));
		Assertions.assertTrue(checker.check(7, errors));

		Assertions.assertFalse(checker.check(9, errors));
		errors.assertSame(List.of("Input should be prime less than 100"));

		Assertions.assertFalse(checker.check(1_000_000_000 + 7, errors));
		errors.assertSame(List.of("Input should be prime less than 100"));
	}

	@Test
	public void validateString_WithExternalCheck_Mapping_Success() {
		TestCheckErrors errors = new TestCheckErrors();
		var checker = Clinq.<String>checker()
			.withBlocking("Input is empty", str -> !str.isEmpty())
			.withBlocking("Not an email", StringMatches.pattern("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"))
			.map(value -> value.split("@")[0])
			.with("Email part before @ is not size of 2", value -> value.length() == 2);

		Assertions.assertFalse(checker.check("sl;dfglksdb", errors));
		errors.assertSame(List.of("Not an email"));

		Assertions.assertFalse(checker.check("a@test.ru", errors));
		errors.assertSame(List.of("Email part before @ is not size of 2"));

		Assertions.assertTrue(checker.check("ab@test.ru", errors));

		Assertions.assertFalse(checker.check("abc@test.ru", errors));
		errors.assertSame(List.of("Email part before @ is not size of 2"));
	}

	@Test
	public void validateDto_WithNestedCheckers_Success() {
		TestCheckErrors errors = new TestCheckErrors();
		var checker =
			Clinq.<ScoresDto>checker()
				.notNull("No data")
				.with(ScoresDto::getName, nameChecker -> nameChecker
					.with("Name is null", Objects::nonNull)
					.with("Name should be bigger than 2 symbols",
						str -> str.length() > 2))
				.with(ScoresDto::getScores, scoresChecker -> scoresChecker
					.with("Scores are null", Objects::nonNull)
					.with("There should be at least 3 scores",
						scores -> scores.size() > 2)
					.with("All scores should be positive",
						scores -> scores.stream().allMatch(score -> score > 0)))
				.with("Name length should be equal to scores size",
					dto -> dto.getName().length() == dto.getScores().size());

		ScoresDto valid = new ScoresDto("hel", List.of(1, 2, 3));
		Assertions.assertTrue(checker.check(valid, errors));

		ScoresDto shortName = new ScoresDto("a", List.of(1, 2, 3));
		Assertions.assertFalse(checker.check(shortName, errors));
		errors.assertSame(List.of(
			"Name should be bigger than 2 symbols",
			"Name length should be equal to scores size"));

		ScoresDto shortScores = new ScoresDto("abc", List.of(1, 2));
		Assertions.assertFalse(checker.check(shortScores, errors));
		errors.assertSame(List.of(
			"There should be at least 3 scores",
			"Name length should be equal to scores size"));

		ScoresDto negativeScores = new ScoresDto("abc", List.of(1, 2, 3, -1));
		Assertions.assertFalse(checker.check(negativeScores, errors));
		errors.assertSame(List.of(
			"All scores should be positive",
			"Name length should be equal to scores size"));
	}

	@Test
	public void validateDto_WithConditionalChecks_Success() {
		TestCheckErrors errors = new TestCheckErrors();
		var shortNameChecker =
			Clinq.<String>checker()
				.with("Short name should start with 's'",
					name -> name.startsWith("s"))
				.mapCheck("Short name's first char should be digit",
					name -> name.charAt(1), Character::isDigit);

		var mediumNameChecker =
			Clinq.<String>checker()
				.with("Medium name should start with 'm'",
					name -> name.startsWith("m"))
				.mapCheck("Medium name's first char should be digit",
					name -> name.charAt(1), Character::isDigit)
				.mapCheck("Medium name's second char should be letter",
					name -> name.charAt(2), Character::isLetter);

		// Name checking attached
		var dtoChecker =
			Clinq.<ScoresDto>checker()
				.with(ScoresDto::getName, it -> it
					.andBlocking("Name's length should be between 1 and 3",
						Objects::nonNull, name -> 1 <= name.length() && name.length() <= 3)
					.when(name -> name.length() <= 2, shortNameChecker)
					.when(name -> name.length() == 3, mediumNameChecker)
				);

		ScoresDto shortNameDto = new ScoresDto("s0", List.of());
		ScoresDto mediumNameDto = new ScoresDto("m0a", List.of());
		ScoresDto invalidDto = new ScoresDto("bigger-than-3", List.of());

		Assertions.assertTrue(dtoChecker.check(shortNameDto, errors));
		Assertions.assertTrue(dtoChecker.check(mediumNameDto, errors));

		Assertions.assertFalse(dtoChecker.check(invalidDto, errors));
		errors.assertSame(List.of("Name's length should be between 1 and 3"));
	}

	@Test
	public void validateDto_WithConditionalChecks_ErrorToken_Success() {
		TestCheckErrors errors = new TestCheckErrors();
		var shortNameChecker =
			Clinq.<String>checker()
				.with(name -> name.startsWith("s"))
					.error("Short name should start with 's'")
				.mapCheck(name -> name.charAt(1), Character::isDigit)
					.error("Short name's first char should be digit");

		var mediumNameChecker =
			Clinq.<String>checker()
				.with(name -> name.startsWith("m"))
					.error("Medium name should start with 'm'")
				.mapCheck(name -> name.charAt(1), Character::isDigit)
					.error("Medium name's first char should be digit")
				.mapCheck(name -> name.charAt(2), Character::isLetter)
					.error("Medium name's second char should be letter");

		// Name checking attached
		var dtoChecker =
			Clinq.<ScoresDto>checker()
				.with(ScoresDto::getName, it -> it
					.and(Objects::nonNull, name -> 1 <= name.length() && name.length() <= 3)
					.error("Name's length should be between 1 and 3")
					.when(name -> name.length() <= 2, shortNameChecker)
					.when(name -> name.length() == 3, mediumNameChecker)
				);

		ScoresDto shortNameDto = new ScoresDto("s0", List.of());
		ScoresDto mediumNameDto = new ScoresDto("m0a", List.of());
		ScoresDto invalidDto = new ScoresDto("bigger-than-3", List.of());

		Assertions.assertTrue(dtoChecker.check(shortNameDto, errors));
		Assertions.assertTrue(dtoChecker.check(mediumNameDto, errors));

		Assertions.assertFalse(dtoChecker.check(invalidDto, errors));
		errors.assertSame(List.of("Name's length should be between 1 and 3"));
	}
}
