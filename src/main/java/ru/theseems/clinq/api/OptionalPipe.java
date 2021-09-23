package ru.theseems.clinq.api;

import java.util.Optional;

public interface OptionalPipe<T, V> {
	Optional<V> produce(T value);
}
