package me.theseems.clinq.api.compiler.error;

import java.util.List;

public interface CheckErrors {
	/**
	 * Add error to all errors there are
	 *
	 * @param error to add
	 */
	void add(Error error);

	/**
	 * Get all errors there are
	 *
	 * @return error list
	 */
	List<Error> getErrors();
}
