package me.theseems.clinq.api;

public interface MapPipe<T, V> {
	V produce(T value);
}
