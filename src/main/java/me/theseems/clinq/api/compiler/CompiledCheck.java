package me.theseems.clinq.api.compiler;

import me.theseems.clinq.api.compiler.error.CheckErrors;

public interface CompiledCheck<T> {
	/**
	 * Check value for compiled constraints
	 *
	 * @param value  to check
	 * @param errors to collect errors in
	 * @return check verdict
	 */
	boolean check(T value, CheckErrors errors);
}
