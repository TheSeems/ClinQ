package me.theseems.clinq.impl.check;

import me.theseems.clinq.api.check.Check;
import me.theseems.clinq.api.check.CheckSettings;

public class ConfiguredCheck<T> implements Check<T> {
	private final CheckSettings settings;
	private final Check<T> wrapped;

	public static <T> Check<T> configured(Check<T> wrapped, CheckSettings settings) {
		if (wrapped instanceof ConfiguredCheck) {
			settings = CheckSettings.merge(((ConfiguredCheck<T>) wrapped).settings, settings);
		}

		return new ConfiguredCheck<>(wrapped, settings);
	}

	public static <T> Check<T> message(Check<T> wrapped, String message) {
		return ConfiguredCheck.configured(wrapped,
			new CheckSettings().setErrorMessage(message));
	}

	public static <T> Check<T> blockMessage(Check<T> wrapped, String message) {
		return ConfiguredCheck.configured(wrapped,
			new CheckSettings().setErrorMessage(message).setPropagate(false));
	}

	public static <T> Check<T> block(Check<T> wrapped) {
		return ConfiguredCheck.configured(wrapped,
			new CheckSettings().setPropagate(false));
	}

	public ConfiguredCheck(Check<T> wrapped, CheckSettings settings) {
		this.wrapped = wrapped;
		this.settings = settings;
	}

	@Override
	public boolean check(T value) {
		return wrapped.check(value);
	}

	@Override
	public CheckSettings settings() {
		return settings;
	}
}
