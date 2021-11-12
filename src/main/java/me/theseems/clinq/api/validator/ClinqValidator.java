package me.theseems.clinq.api.validator;

import lombok.SneakyThrows;
import me.theseems.clinq.api.checker.Checker;
import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.impl.compiler.error.IgnoreCheckErrors;

public abstract class ClinqValidator<T> {
	private Checker<T, ?> checker;

	public ClinqValidator() {
	}

	/**
	 * Bake checker
	 *
	 * @return backed checker
	 * @implNote this should be called once
	 */
	public abstract Checker<T, ?> bake();

	/**
	 * Check the input valued
	 *
	 * @param input  to check
	 * @param errors to collect errors
	 * @return check verdict
	 */
	public boolean check(T input, CheckErrors errors) {
		if (checker == null) {
			checker = bake();
		}

		return checker.check(input, errors);
	}

	@SneakyThrows
	public static <T> ClinqValidator<T> of(Class<? extends ClinqValidator<T>> validatorClass) {
		return validatorClass.getConstructor().newInstance();
	}

	@SneakyThrows
	public static <T> boolean check(Class<? extends ClinqValidator<T>> validatorClass, T value, CheckErrors errors) {
		return of(validatorClass).check(value, errors);
	}

	@SneakyThrows
	public static <T> boolean check(Class<? extends ClinqValidator<T>> validatorClass, T value) {
		return of(validatorClass).check(value, new IgnoreCheckErrors());
	}
}
