package ru.vbc.clinq.api;

public interface MapPipe<T, V> {
	V produce(T value);
}
