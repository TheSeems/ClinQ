package me.theseems.clinq.api.compiler;

import me.theseems.clinq.api.compiler.error.CheckErrors;

public interface CompiledCheck<T> {
	boolean check(T value, CheckErrors errors);
}
