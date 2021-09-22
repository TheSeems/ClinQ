package ru.vbc.clinq.impl.compiler;

public final class TokenCompilerSettings {
	// Package-private for compiler accessibility
	int tokenCountLimit = 5000;

	public TokenCompilerSettings tokenCountLimit(int limit) {
		this.tokenCountLimit = limit;
		return this;
	}
}
