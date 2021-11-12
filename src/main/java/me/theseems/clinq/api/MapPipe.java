package me.theseems.clinq.api;

public interface MapPipe<T, V> {
	/**
	 * Mapping pipe: T -> V
	 *
	 * @param value to map
	 * @return mapped value
	 */
	V produce(T value);
}
