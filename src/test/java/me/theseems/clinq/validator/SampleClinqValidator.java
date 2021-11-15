package me.theseems.clinq.validator;

import me.theseems.clinq.api.Clinq;
import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.api.validator.ClinqValidator;
import me.theseems.clinq.impl.check.ConfiguredCheck;

import java.util.Objects;

public class SampleClinqValidator extends ClinqValidator<String> {
	@Override
	public Checker<String, ?> bake() {
		return Clinq.<String>checker()
			.with(notNull(String.class))
			.with(str -> str.length() == 2);
	}

	public <T> Check<T> notNull(Class<T> value) {
		return ConfiguredCheck.blockMessage(Objects::nonNull, value.getName() + " is null");
	}
}
