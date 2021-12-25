package me.theseems.clinq.validator;

import me.theseems.clinq.api.validator.ClinqValidator;
import me.theseems.clinq.test.TestCheckErrors;
import me.theseems.clinq.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SampleClinqValidatorTest {
	@Test
	public void instantiateValidator_Check_Success() {
		ClinqValidator<String> validator = new SampleClinqValidator();
		Assertions.assertFalse(validator.check("hello"));
	}

	@Test
	public void instantiateValidator_WithOf_Check_Success() {
		ClinqValidator<String> validator = ClinqValidator.of(SampleClinqValidator.class);
		StringUtils.randomStrings(20, 2, 5)
			.forEach(str -> Assertions.assertEquals(
				str.length() == 2, validator.check(str),
				"Check for '" + str + "' failed"));
	}

	@Test
	public void instantiateValidator_Check_Null_Failure() {
		ClinqValidator<String> validator = new SampleClinqValidator();
		TestCheckErrors errors = new TestCheckErrors();
		validator.check((String) null, errors);

		errors.assertSame(List.of("java.lang.String is null"));
	}
}
