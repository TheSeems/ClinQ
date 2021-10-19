package me.theseems.clinq.impl.token;

import me.theseems.clinq.api.OptionalPipe;
import me.theseems.clinq.impl.compiler.visit.VisitResult;

public class PipeToken<T, V> implements Token {
	private final OptionalPipe<T, V> pipe;

	public PipeToken(OptionalPipe<T, V> pipe) {
		this.pipe = pipe;
	}

	public OptionalPipe<T, V> getPipe() {
		return pipe;
	}

	@Override
	public VisitResult accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
