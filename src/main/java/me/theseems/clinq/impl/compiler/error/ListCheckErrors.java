package me.theseems.clinq.impl.compiler.error;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.api.compiler.error.Error;

import java.util.ArrayList;
import java.util.List;

public class ListCheckErrors implements CheckErrors {
	private final List<Error> errors;

	public ListCheckErrors() {
		errors = new ArrayList<>();
	}

	@Override
	public void add(Error error) {
		errors.add(error);
	}

	@Override
	public List<Error> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return "ListCheckErrors{" +
			"errors=" + errors +
			'}';
	}
}
