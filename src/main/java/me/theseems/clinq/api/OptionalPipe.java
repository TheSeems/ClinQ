package me.theseems.clinq.api;

import me.theseems.clinq.api.check.CheckSettings;

import java.util.Optional;

public interface OptionalPipe<T, V> {
	default CheckSettings settings() {
		return new CheckSettings();
	}

	Optional<V> produce(T value);
}
