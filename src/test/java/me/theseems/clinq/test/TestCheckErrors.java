package me.theseems.clinq.test;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.api.compiler.error.Error;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestCheckErrors implements CheckErrors {
	private final List<Error> errors;

	public TestCheckErrors() {
		errors = new ArrayList<>();
	}

	@Override
	public void add(Error error) {
		errors.add(error);
	}

	@Override
	public List<Error> getErrors() {
		return errors;
	}

	public void clear() {
		errors.clear();
	}

	public void assertSame(List<String> errors) {
		List<String> current = this.errors.stream()
			.map(Error::getDescription)
			.collect(Collectors.toList());

		var errorArray = errors.toArray(new String[]{});
		var currentArray = current.toArray(new String[]{});

		Assertions.assertEquals(errorArray.length, currentArray.length,
			String.format("Expected: %s. Actual: %s",
				Arrays.toString(errorArray),
				Arrays.toString(currentArray)));

		Assertions.assertArrayEquals(errorArray, currentArray);
		clear();
	}
}
