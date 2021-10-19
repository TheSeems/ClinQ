package me.theseems.clinq.api.compiler.error;

import java.util.List;

public interface CheckErrors {
	void add(Error error);

	List<Error> getErrors();
}
