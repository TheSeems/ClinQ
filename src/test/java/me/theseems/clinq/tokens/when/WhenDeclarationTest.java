package me.theseems.clinq.tokens.when;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.compiler.exception.CompileError;
import me.theseems.clinq.test.TestCheckErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WhenDeclarationTest {
	@Test
	public void simpleWhen_Success() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2)
				.then("Hey! Start with 's', please!", str -> str.startsWith("s"))
				.orElse("Hey! Start with 'b', please!", str -> str.startsWith("b"));

		TestCheckErrors testCheckErrors = new TestCheckErrors();
		checker.check("aba", testCheckErrors);

		testCheckErrors.assertSame(List.of("Hey! Start with 'b', please!"));
	}

	@Test
	public void when_WithNoThenOrElse_Failure() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2);

		Assertions.assertThrows(CompileError.class, () -> checker.check("aba"));
	}

	@Test
	public void when_WithThenNoElse_Success() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2)
			.then(o -> o
				.with(str -> str.contains("a"))
				.and(str -> str.split("a")[0].equals("b")));

		Assertions.assertTrue(checker.check("aba"));
	}

	@Test
	public void when_WithThenAndElse_Success() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2)
				.then(o -> o
					.with(str -> str.contains("a"))
					.and(str -> str.split("a")[0].equals("b")))
				.orElse(o -> o
					.with(str -> str.contains("b"))
					.and(str -> str.split("b")[0].equals("a")));

		Assertions.assertTrue(checker.check("aba"));
	}

	@Test
	public void when_WithThenAndElse_Checks_Success() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2)
				.thenCheck(success())
				.orElseCheck(fail());

		Assertions.assertTrue(checker.check("ab"));
	}

	private <T> Check<T> fail() {
		return value -> {
			Assertions.fail();
			return false;
		};
	}

	private <T> Check<T> success() {
		return value -> true;
	}
}
