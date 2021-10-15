package me.theseems.clinq.impl.compiler.visit;

import lombok.Builder;
import lombok.Value;
import me.theseems.clinq.api.compiler.error.Error;

import java.util.List;

@Value
@Builder
public class VisitResult {
	boolean success;

	@Builder.Default
	List<Error> errors = null;

	@Builder.Default
	boolean propagate = true;
}
