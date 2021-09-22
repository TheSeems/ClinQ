package ru.theseems.clinq.impl.compiler;

public final class TokenCompilerSettings {
	// Package-private for compiler accessibility
	int tokenCountLimit = 5000;
	boolean enableExceptionOnFail = false;

	public TokenCompilerSettings tokenCountLimit(int limit) {
		this.tokenCountLimit = limit;
		return this;
	}

	public TokenCompilerSettings enableExceptionOnFail() {
		this.enableExceptionOnFail = true;
		return this;
	}
}
