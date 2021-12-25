package me.theseems.clinq.test;

import me.theseems.clinq.api.compiler.error.CheckErrors;
import me.theseems.clinq.api.compiler.error.Error;

import java.util.Collections;
import java.util.List;

public class PrintErrors implements CheckErrors {
	@Override
	public void add(Error error) {
		System.out.println("Error: " + error.getDescription() + " (" + error + ")");
	}

	@Override
	public List<Error> getErrors() {
		return Collections.emptyList();
	}
}
