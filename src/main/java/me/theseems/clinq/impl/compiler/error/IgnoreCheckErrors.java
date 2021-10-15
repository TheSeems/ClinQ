package me.theseems.clinq.impl.compiler.error;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.api.compiler.error.Error;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCheckErrors implements CheckErrors {

	@Override
	public void add(Error error) {
	}

	@Override
	public List<Error> getErrors() {
		return new ArrayList<>();
	}
}
