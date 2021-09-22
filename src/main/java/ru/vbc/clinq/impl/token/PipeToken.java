package ru.vbc.clinq.impl.token;

import ru.vbc.clinq.api.OptionalPipe;

public class PipeToken<T, V> implements Token {
	private final OptionalPipe<T, V> pipe;

	public PipeToken(OptionalPipe<T, V> pipe) {
		this.pipe = pipe;
	}

	public OptionalPipe<T, V> getPipe() {
		return pipe;
	}

	@Override
	public boolean accept(TokenVisitor visitor) {
		return visitor.visit(this);
	}
}
