package me.theseems.clinq.api.compiler;

import lombok.Data;

@Data
public final class TokenCompilerSettings {
	// Package-private for compiler accessibility
	int tokenCountLimit = 5000;
	boolean exceptionOnFail = false;

	public TokenCompilerSettings tokenCountLimit(int limit) {
		this.tokenCountLimit = limit;
		return this;
	}

	public TokenCompilerSettings exceptionOnFail() {
		this.exceptionOnFail = true;
		return this;
	}
}
