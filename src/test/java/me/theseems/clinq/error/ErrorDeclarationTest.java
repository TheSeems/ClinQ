package me.theseems.clinq.error;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.test.TestCheckErrors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ErrorDeclarationTest {
	@Test
	public void check_WithNamedErrors_InPlace_Success() {
		var checker = Clinq.<String>checker()
			.notNull("Input is null")
			.and("Length should be at least 2", name -> name.length() >= 2)
			.mapCheck("First char should be digit", name -> name.charAt(0), Character::isDigit)
			.mapCheck("Second char should be letter", name -> name.charAt(1), Character::isLetter);

		TestCheckErrors errors = new TestCheckErrors();
		checker.check("i1nvalid", errors);
		errors.assertSame(List.of("First char should be digit", "Second char should be letter"));

		checker.check("1bAnything", errors);
		Assertions.assertEquals(0, errors.getErrors().size());
	}

	@Test
	public void check_WithNamedErrors_OutOfPlace_Success() {
		var checker = Clinq.<String>checker()
			.notNull("Input is null")
			.and(name -> name.length() >= 2)
				.error("Length should be at least 2")
			.mapCheck(name -> name.charAt(0), Character::isDigit)
				.error("First char should be digit")
			.mapCheck(name -> name.charAt(1), Character::isLetter)
				.error("Second char should be letter");

		TestCheckErrors errors = new TestCheckErrors();
		checker.check("i1nvalid", errors);
		errors.assertSame(List.of("First char should be digit", "Second char should be letter"));

		checker.check("1bAnything", errors);
		Assertions.assertEquals(0, errors.getErrors().size());
	}
}
