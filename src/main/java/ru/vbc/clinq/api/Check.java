package ru.vbc.clinq.api;

import java.util.Optional;

public interface Check<T> {
	boolean check(T value);
}
