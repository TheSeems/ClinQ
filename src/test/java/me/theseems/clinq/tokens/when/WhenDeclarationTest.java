package me.theseems.clinq.tokens.when;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.check.Check;
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

		testCheckErrors.assertSame(List.of("Error: Hey! Start with 'b', please!"));
	}

	@Test
	public void when_WithNoElse_Success() {
		var checker = Clinq.<String>checker();

		checker
			.with(str -> str.length() <= 3).blocking()
			.when(str -> str.length() == 2)
				.thenCheck(fail());

		checker.check("aba");
	}

	private <T> Check<T> fail() {
		return value -> {
			Assertions.fail();
			return false;
		};
	}
}
