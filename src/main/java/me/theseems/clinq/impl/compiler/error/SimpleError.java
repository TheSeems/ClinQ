package me.theseems.clinq.impl.compiler.error;

import lombok.Value;
import me.theseems.clinq.api.compiler.error.Error;

@Value
public class SimpleError implements Error {
	String description;

	public static SimpleError of(String description) {
		return new SimpleError(description);
	}
}
