package ru.vbc.clinq.api;

import java.util.Optional;

public interface MapPipe<T, V> {
	V produce(T value);
}
