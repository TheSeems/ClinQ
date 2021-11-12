package me.theseems.clinq.api;

import me.theseems.clinq.api.check.CheckSettings;

import java.util.Optional;

public interface OptionalPipe<T, V> {
	/**
	 * Produce optional by mapping
	 *
	 * @param value to map
	 * @return mapped-optional value
	 */
	Optional<V> produce(T value);

	/**
	 * Settings of the pipe
	 *
	 * @return settings
	 */
	default CheckSettings settings() {
		return new CheckSettings();
	}
}
